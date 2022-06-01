package com.company.Repositories;

import com.company.IQuestionnaire;
import com.company.Question;
import com.company.User.User;

import javax.naming.AuthenticationException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

public class AccountPermission implements IQuestionnaireRepository{
    User user = null;
    QuestionnaireRepository repository;

    public AccountPermission(User user, QuestionnaireRepository repository) {
        this.user = user;
        this.repository = repository;
    }

    @Override
    public boolean addQuestionnaire() throws SQLException, IllegalAccessException, AuthenticationException {
        if(user == null) throw new AuthenticationException("Authenticate in order to access this resource");
        else if(user.isAdmin()) return repository.addQuestionnaire();
        else throw new IllegalAccessException("Permission denied");
    }

    @Override
    public List<IQuestionnaire> getQuestionnaires() throws SQLException {
        return repository.getQuestionnaires();
    }

    @Override
    public boolean addQuestion(int id, String questionText) throws SQLException, IllegalAccessException, AuthenticationException {
        if(user == null) throw new AuthenticationException("Authenticate in order to access this resource");
        else if(user.isAdmin()) return repository.addQuestion(id, questionText);
        else throw new IllegalAccessException("Permission denied");
    }

    @Override
    public String getQuestionnaireById(int id) throws SQLException {
        return repository.getQuestionnaireById(id);
    }

    @Override
    public boolean addAnswer(int id, String answerText, boolean isCorrect) throws SQLException, IllegalAccessException, AuthenticationException {
        if(user == null) throw new AuthenticationException("Authenticate in order to access this resource");
        else if (user.isAdmin()) return repository.addAnswer(id, answerText, isCorrect);
        else throw new IllegalAccessException("Permission denied");
    }

    @Override
    public List<Question> getQuestions(int questionnaireId) throws SQLException {
        return repository.getQuestions(questionnaireId);
    }

    @Override
    public Hashtable<String, Boolean> getAnswers(int questionId) throws SQLException {
        return repository.getAnswers(questionId);
    }

    @Override
    public boolean addScore(int userId, int questionnaireId, int score) throws SQLException, AuthenticationException, IllegalAccessException {
        if(user == null) throw new AuthenticationException("Authenticate in order to access this resource");
        else if(!user.isAdmin()) return repository.addScore(userId, questionnaireId, score);
        else throw new IllegalAccessException("Admin can't answer questionnaires");
    }

    @Override
    public boolean updateScore(int userId, int score) {
        return false;
    }

    @Override
    public Question getQuestionByText(String questionText) throws SQLException {
        return repository.getQuestionByText(questionText);
    }
}
