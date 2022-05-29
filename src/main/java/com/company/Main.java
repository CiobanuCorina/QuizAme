package com.company;

import com.company.Repositories.UserRepository;
import com.company.User.User;
import com.company.User.UserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import javax.naming.SizeLimitExceededException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;;

public class Main {
    static String username;
    static String password;
    public static void main(String[] args) throws SizeLimitExceededException, IOException, InterruptedException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/register", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper mapper = new ObjectMapper();
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    username = (String) map.get("username");
                    password = (String) map.get("password");
                    User newUser = new User(username,
                            password,
                            (String) map.get("email"),
                            UserRoles.USER);
                    UserRepository repository = new UserRepository(newUser);
                    if(repository.addUser()) {
                        exchange.sendResponseHeaders(201, -1);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/login", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper mapper = new ObjectMapper();
                String text = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                try {
                    Map map = mapper.readValue(text, Map.class);
                    username = (String) map.get("username");
                    password = (String) map.get("password");
                    UserRepository repository = new UserRepository();
                    User user = repository.getUser(username, password);
                    if(user != null) {
                        exchange.sendResponseHeaders(200, -1);
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/logout", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                username = null;
                password = null;
                exchange.sendResponseHeaders(200, -1);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));
        server.createContext("/", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Questionnaire questionnaire = new Questionnaire();
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
        server.createContext("/hint", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Questionnaire questionnaire = new Questionnaire();
                questionnaire.addQuestion(new Question("dfvkjtu", new Hashtable<>() {{ put("djfhrug", false); put("djfhgu", true); put("dfrgsze", false); put("dsrgsx", false);put("dcszdz", true);put("xnmdfjh", true);}}));
                questionnaire.addQuestion(new Question("drgthr", new Hashtable<>() {{ put("dth", true); put("gfbf", false);}}));

                IQuestionnaire newQuestionnaire = new HintQuestionnaire(questionnaire);
                String respText = null;
                try {
                    respText = newQuestionnaire.ask();
                } catch (InterruptedException | SizeLimitExceededException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
}
