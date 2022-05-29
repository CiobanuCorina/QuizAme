package com.company;

import com.company.Iterator.AnswersFactory;
import com.company.Iterator.Iterator;
import com.company.Iterator.IteratorFactory;
import com.fasterxml.jackson.annotation.JsonGetter;

import javax.naming.SizeLimitExceededException;
import java.util.Hashtable;
import java.util.Map;

public class Question{
    private String questionText;
    private Hashtable<String, Boolean> answers;

    public Question(String questionText, Hashtable<String, Boolean> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public boolean checkAnswer(String answer) throws SizeLimitExceededException {
        boolean result = false;
        IteratorFactory<Hashtable<String, Boolean>> iteratorFactory = new AnswersFactory();
        Iterator iterator = iteratorFactory.getIterator(answers);
        while(iterator.hasMore()) {
            Map.Entry<String, Boolean> ans = iterator.getNext();
            if(ans.getKey().equals(answer)) {
                result = ans.getValue();
            }
        }
        return result;
    }

    @JsonGetter(value = "answers")
    public Map.Entry[] getMap() {
        return answers.entrySet().toArray(new Map.Entry[]{});
    }

    public String getQuestionText() {
        return questionText;
    }

    public Hashtable<String, Boolean> getAnswers() {
        return answers;
    }
}
