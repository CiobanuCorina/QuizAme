package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.naming.SizeLimitExceededException;
import java.util.List;

public class QuestionnaireDecorator implements IQuestionnaire{
    IQuestionnaire wrappee;

    public QuestionnaireDecorator(IQuestionnaire wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String ask() throws JsonProcessingException, InterruptedException, SizeLimitExceededException, IllegalAccessException {
        return wrappee.ask();
    }

    @Override
    public boolean checkAnswer(String question, String answer) throws SizeLimitExceededException, IllegalAccessException {
        return wrappee.checkAnswer(question, answer);
    }

    @Override
    public void addQuestion(Question question) throws IllegalAccessException {
        wrappee.addQuestion(question);
    }

    @Override
    public void removeQuestion(int index) throws IllegalAccessException {
        wrappee.removeQuestion(index);
    }

    @Override
    public List<Question> getQuestions() throws IllegalAccessException {
        return wrappee.getQuestions();
    }

    @Override
    public String getName() throws IllegalAccessException {
        return wrappee.getName();
    }
}
