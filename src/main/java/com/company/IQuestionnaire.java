package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.naming.SizeLimitExceededException;
import java.util.List;

public interface IQuestionnaire {
    String ask() throws JsonProcessingException, InterruptedException, SizeLimitExceededException, IllegalAccessException;
    boolean checkAnswer(String question, String answer) throws SizeLimitExceededException, IllegalAccessException;
    void addQuestion(Question question) throws IllegalAccessException;
    void removeQuestion(int index) throws IllegalAccessException;
    List<Question> getQuestions() throws IllegalAccessException;
    String getName() throws IllegalAccessException;
}
