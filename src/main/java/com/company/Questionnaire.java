package com.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Questionnaire implements IQuestionnaire{
    private int id;
    private String name;
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    public Questionnaire(String name) {
        this.name = name;
    }

    public Questionnaire(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Questionnaire(int id, String name, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
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

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
