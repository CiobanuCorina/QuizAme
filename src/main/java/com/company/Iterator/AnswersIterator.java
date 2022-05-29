package com.company.Iterator;

import javax.naming.SizeLimitExceededException;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class AnswersIterator implements Iterator{
    private Hashtable<String, Boolean> answerList;
    private Enumeration<String> answers;

    public AnswersIterator(Hashtable<String, Boolean> answerList) {
        this.answerList = answerList;
        this.answers = answerList.keys();
    }

    @Override
    public Map.Entry<String, Boolean> getNext() throws SizeLimitExceededException {
        if(this.hasMore()) {
            String key = answers.nextElement();
           return new AbstractMap.SimpleEntry<>(key, answerList.get(key));
        }
        throw new SizeLimitExceededException("Index out of bound");
    }

    @Override
    public boolean hasMore() {
        return answers.hasMoreElements();
    }
}
