package com.company;

import com.company.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.naming.SizeLimitExceededException;
import java.util.List;

public class Account implements IQuestionnaire{
    User user;
    IQuestionnaire questionnaire;

    public Account(User user, IQuestionnaire questionnaire) {
        this.user = user;
        this.questionnaire = questionnaire;
    }

    @Override
    public String ask() throws JsonProcessingException, InterruptedException, SizeLimitExceededException, IllegalAccessException {
        if(!user.isAdmin()) return questionnaire.ask();
        else throw new IllegalAccessException("Admin can't answer questions");
    }

    @Override
    public boolean checkAnswer(String question, String answer) throws SizeLimitExceededException, IllegalAccessException {
        if(!user.isAdmin()) return questionnaire.checkAnswer(question, answer);
        else throw new IllegalAccessException("Admin can't check answers");
    }

    @Override
    public void addQuestion(Question question) throws IllegalAccessException {
        if(user.isAdmin()) questionnaire.addQuestion(question);
        else throw new IllegalAccessException("Unauthorized access");
    }

    @Override
    public void removeQuestion(int index) throws IllegalAccessException {
        if(user.isAdmin()) questionnaire.removeQuestion(index);
        else throw new IllegalAccessException("Unauthorized access");
    }

    @Override
    public List<Question> getQuestions() throws IllegalAccessException {
        if(user.isAdmin()) return questionnaire.getQuestions();
        else throw new IllegalAccessException("Unauthorized access");
    }

    @Override
    public String getName() throws IllegalAccessException {
        if(user.isAdmin()) return questionnaire.getName();
        else throw new IllegalAccessException("Unauthorized access");
    }
}
