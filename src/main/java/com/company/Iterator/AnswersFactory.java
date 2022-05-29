package com.company.Iterator;

import java.util.Hashtable;
import java.util.List;

public class AnswersFactory<T> implements IteratorFactory<Hashtable<String, Boolean>>{
    @Override
    public Iterator getIterator(Hashtable<String, Boolean> answerList) {
        return new AnswersIterator(answerList);
    }
}
