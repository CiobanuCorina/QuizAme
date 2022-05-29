package com.company;

import com.company.Iterator.AnswersFactory;
import com.company.Iterator.Iterator;
import com.company.Iterator.IteratorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.naming.SizeLimitExceededException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HintQuestionnaire extends QuestionnaireDecorator{
    public HintQuestionnaire(IQuestionnaire wrappee) {
        super(wrappee);
    }

    @Override
    public String ask() throws JsonProcessingException, InterruptedException, SizeLimitExceededException, IllegalAccessException {
        List<Question> questions = super.getQuestions();
        for (Question question: questions) {
            int counter = 0;
            Hashtable<String, Boolean> answers = question.getAnswers();
            IteratorFactory<Hashtable<String, Boolean>> iteratorFactory = new AnswersFactory();
            Iterator iterator = iteratorFactory.getIterator(answers);
            while(iterator.hasMore() && answers.size() != 2) {
                if(counter == answers.size()/2) break;
                Map.Entry<String, Boolean> ans = iterator.getNext();
                if(!ans.getValue()) answers.remove(ans.getKey());
            }
        }
        return super.ask();
    }
}
