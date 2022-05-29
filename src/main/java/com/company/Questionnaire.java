package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class Questionnaire implements IQuestionnaire{
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    private int id;
    private List<Question> questions = new ArrayList<>();

    public Questionnaire() {
        this.id = ID_GENERATOR.getAndIncrement();
    }

    @Override
    public String ask() throws JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.writeValueAsString(questions);
    }

    @Override
    public boolean checkAnswer(String question, String answer) throws SizeLimitExceededException {
        for (Question quest:questions) {
            if(quest.getQuestionText().equals(question))
                return quest.checkAnswer(answer);
        }
        throw new NoSuchElementException("There is no such question");
    }

    @Override
    public void addQuestion(Question question) {
        questions.add(question);
    }

    @Override
    public void removeQuestion(int index) {
        questions.remove(index);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
