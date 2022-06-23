package com.company.Repositories;

import com.company.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class QuestionnaireRepository implements IQuestionnaireRepository
{
    Connection connection = DBConnection.getDbConnection().getConnection();
    IQuestionnaire questionnaire;

    public QuestionnaireRepository(IQuestionnaire questionnaire) throws IOException {
        this.questionnaire = questionnaire;
    }

    public QuestionnaireRepository() throws IOException {}
    @Override
    public boolean addQuestionnaire() throws SQLException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quiz_game.questionnaire(name) VALUES(?)");
        preparedStatement.setString(1, questionnaire.getName());
        return preparedStatement.executeUpdate() == 1;
    }

    @Override
    public List<IQuestionnaire> getQuestionnaires() throws SQLException {
        List<IQuestionnaire> questionnaireList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_game.questionnaire");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            questionnaireList.add(new Questionnaire(resultSet.getInt("id"), resultSet.getString("name")));
        }
        return questionnaireList;
    }

    @Override
    public boolean addQuestion(int id, String questionText) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quiz_game.question(question_text, fk_questionnaire_id) VALUES(?, ?)");
        preparedStatement.setString(1, questionText);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate() == 1;
    }

    @Override
    public String getQuestionnaireById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_game.questionnaire WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("name");
    }

    @Override
    public boolean addAnswer(int id, String answerText, boolean isCorrect) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quiz_game.answer(answer_text, is_correct, fk_question_id) VALUES(?, ?, ?)");
        preparedStatement.setString(1, answerText);
        preparedStatement.setBoolean(2, isCorrect);
        preparedStatement.setInt(3, id);
        return preparedStatement.executeUpdate() == 1;
    }

    @Override
    public List<Question> getQuestions(int questionnaireId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_game.question WHERE fk_questionnaire_id = ?");
        preparedStatement.setInt(1, questionnaireId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            questions.add(new Question(resultSet.getString("question_text"), getAnswers(resultSet.getInt("id"))));
        }
        return questions;
    }

    @Override
    public Hashtable<String, Boolean> getAnswers(int questionId) throws SQLException {
        Hashtable<String, Boolean> answers = new Hashtable<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_game.answer WHERE fk_question_id = ?");
        preparedStatement.setInt(1, questionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            answers.put(resultSet.getString("answer_text"), resultSet.getBoolean("is_correct"));
        }
        return answers;
    }

    @Override
    public boolean addScore(int userId, int questionnaireId, int score) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quiz_game.user_questionnaire(fk_user_id, fk_questionnaire_id, score) VALUES(?, ?, ?)");
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, questionnaireId);
        preparedStatement.setInt(3, score);
        return preparedStatement.executeUpdate() == 1;
    }

    @Override
    public Question getQuestionByText(String questionText) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_game.question WHERE question_text = ?");
        preparedStatement.setString(1, questionText);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return new Question(resultSet.getString("question_text"), getAnswers(resultSet.getInt("id")));
    }

    @Override
    public boolean updateScore(int userId, int score) {
        return false;
    }
}
