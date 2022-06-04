package com.company;

import com.company.Levels.Beginner;
import com.company.Levels.Expert;
import com.company.Levels.Intermediate;
import com.company.Levels.LevelFactory;
import com.company.Repositories.AccountPermission;
import com.company.Repositories.IQuestionnaireRepository;
import com.company.Repositories.QuestionnaireRepository;
import com.company.Repositories.UserRepository;
import com.company.User.User;
import com.company.User.UserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import javax.naming.AuthenticationException;
import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;;

public class Main {
    static User user = null;
    public static void main(String[] args) throws SizeLimitExceededException, IOException, InterruptedException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        EventManager events = new EventManager();
        Subscriber emailSender = new RegisterEmailNotification();
        events.subscribe("register email", emailSender);
        server.createContext("/register", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                OutputStream output;
                ObjectMapper mapper = new ObjectMapper();
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    System.out.println(map.get("params"));
                    user = new User((String) map.get("username"),
                            (String) map.get("password"),
                            (String) map.get("email"),
                            UserRoles.USER);
                    UserRepository repository = new UserRepository(user);
                    if(repository.addUser()) {
                        user = repository.getUser((String) map.get("username"), (String) map.get("password"));
                        String jsonUser = mapper.writeValueAsString(user);
                        exchange.sendResponseHeaders(201, jsonUser.getBytes().length);
                        output = exchange.getResponseBody();
                        output.write(jsonUser.getBytes());
                        output.flush();
                        events.notifyListeners("register email", user);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else if("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(200, -1);
            }else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/login", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                OutputStream output;
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                ObjectMapper mapper = new ObjectMapper();
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    UserRepository repository = new UserRepository();
                    user = repository.getUser((String) map.get("username"), (String) map.get("password"));
                    if(user != null) {
                        String jsonUser = mapper.writeValueAsString(user);
                        exchange.sendResponseHeaders(200, jsonUser.getBytes().length);
                        output = exchange.getResponseBody();
                        output.write(jsonUser.getBytes());
                        output.flush();
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else if("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(200, -1);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/logout", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                user = null;
                exchange.sendResponseHeaders(200, -1);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/questionnaire", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                ObjectMapper mapper = new ObjectMapper();
                OutputStream output;
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository(new Questionnaire((String) map.get("name"))));
                    if(questionnaireRepository.addQuestionnaire()) {
                        exchange.sendResponseHeaders(201, -1);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException | IllegalAccessException | AuthenticationException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/questionnaires", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                ObjectMapper mapper = new ObjectMapper();
                StringWriter writer =new StringWriter();
                IQuestionnaireRepository questionnaireRepository = new QuestionnaireRepository();
                try {
                    List<IQuestionnaire> questionnaireList = questionnaireRepository.getQuestionnaires();
                    mapper.writeValue(writer, questionnaireList);
                    exchange.sendResponseHeaders(200, writer.toString().getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(writer.toString().getBytes());
                    output.flush();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/question", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper mapper = new ObjectMapper();
                OutputStream output;
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                    if(questionnaireRepository.addQuestion(Integer.parseInt(params.get("id")), (String) map.get("questionText"))) {
                        exchange.sendResponseHeaders(201, -1);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException | IllegalAccessException | AuthenticationException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/answer", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper mapper = new ObjectMapper();
                OutputStream output;
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                    if(questionnaireRepository.addAnswer(Integer.parseInt(params.get("id")),
                            (String) map.get("answerText"),
                            (Boolean) map.get("isCorrect"))) {
                        exchange.sendResponseHeaders(201, -1);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException | IllegalAccessException | AuthenticationException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/intermediate", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                OutputStream output;
                try {
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                    List<Question> questions = questionnaireRepository.getQuestions(Integer.parseInt(params.get("id")));
                    String questionnaireName = questionnaireRepository.getQuestionnaireById(Integer.parseInt(params.get("id")));
                    LevelFactory level = new Intermediate();
                    IQuestionnaire questionnaire = level.wrapQuestionnaire(Integer.parseInt(params.get("id")), questionnaireName, questions);
                    String json = questionnaire.ask();
                    exchange.sendResponseHeaders(200, json.getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(json.getBytes());
                    output.flush();
                } catch (SQLException | SizeLimitExceededException | InterruptedException | IllegalAccessException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/beginner", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                OutputStream output;
                try {
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                    List<Question> questions = questionnaireRepository.getQuestions(Integer.parseInt(params.get("id")));
                    String questionnaireName = questionnaireRepository.getQuestionnaireById(Integer.parseInt(params.get("id")));
                    LevelFactory level = new Beginner();
                    IQuestionnaire questionnaire = level.wrapQuestionnaire(Integer.parseInt(params.get("id")), questionnaireName, questions);
                    String json = questionnaire.ask();
                    exchange.sendResponseHeaders(200, json.getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(json.getBytes());
                    output.flush();
                } catch (SQLException | SizeLimitExceededException | InterruptedException | IllegalAccessException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/expert", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                OutputStream output;
                try {
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                    List<Question> questions = questionnaireRepository.getQuestions(Integer.parseInt(params.get("id")));
                    String questionnaireName = questionnaireRepository.getQuestionnaireById(Integer.parseInt(params.get("id")));
                    LevelFactory level = new Expert();
                    IQuestionnaire questionnaire = level.wrapQuestionnaire(Integer.parseInt(params.get("id")), questionnaireName, questions);
                    String json = questionnaire.ask();
                    exchange.sendResponseHeaders(200, json.getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(json.getBytes());
                    output.flush();
                } catch (SQLException | SizeLimitExceededException | InterruptedException | IllegalAccessException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/check", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper mapper = new ObjectMapper();
                OutputStream output;
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    Question question = questionnaireRepository.getQuestionByText((String) map.get("questionText"));
                    boolean isCorrect = question.checkAnswer((String) map.get("answer"));
                    String json = "{\"isCorrect\":" + isCorrect + "}";
                    exchange.sendResponseHeaders(200, json.getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(json.getBytes());
                    output.flush();
                } catch (IOException | SQLException | SizeLimitExceededException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/score", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper mapper = new ObjectMapper();
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                OutputStream output;
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    IQuestionnaireRepository questionnaireRepository = new AccountPermission(user, new QuestionnaireRepository());
                    if(user == null) throw new javax.security.sasl.AuthenticationException("Authenticate in order to access this resource");
                    System.out.println(user.getId());
                    if(questionnaireRepository.addScore(user.getId(), (int) map.get("questionnaireId"), (int) map.get("score"))){
                        exchange.sendResponseHeaders(201, -1);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException | AuthenticationException | IllegalAccessException e) {
                    exchange.sendResponseHeaders(417, e.getMessage().getBytes().length);
                    output = exchange.getResponseBody();
                    output.write(e.getMessage().getBytes());
                    output.flush();
                    e.printStackTrace();
                }
            } else if("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(200, -1);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Questionnaire questionnaire = new Questionnaire("test");
                questionnaire.addQuestion(new Question("dfvkjtu", new Hashtable<>() {{ put("djfhrug", false); put("djfhgu", true);}}));
                questionnaire.addQuestion(new Question("drgthr", new Hashtable<>() {{ put("dth", true); put("gfbf", false);}}));

                String respText = questionnaire.ask();
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.setExecutor(null);
        server.start();
    }
    public static Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
