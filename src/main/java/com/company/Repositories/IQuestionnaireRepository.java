package com.company.Repositories;

import com.company.IQuestionnaire;
import com.company.Question;

import javax.naming.AuthenticationException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

public interface IQuestionnaireRepository {
    public boolean addQuestionnaire() throws SQLException, IllegalAccessException, AuthenticationException;
    public List<IQuestionnaire> getQuestionnaires() throws SQLException;
    public boolean addQuestion(int id, String questionText) throws SQLException, IllegalAccessException, AuthenticationException;
    public String getQuestionnaireById(int id) throws SQLException;
    public boolean addAnswer(int id, String answerText, boolean isCorrect) throws SQLException, IllegalAccessException, AuthenticationException;
    public List<Question> getQuestions(int questionnaireId) throws SQLException;
    public Hashtable<String, Boolean> getAnswers(int questionId) throws SQLException;
    public boolean addScore(int userId, int questionnaireId, int score) throws SQLException, AuthenticationException, IllegalAccessException;
    public boolean updateScore(int userId, int score);
    public Question getQuestionByText(String questionText) throws SQLException;
}
