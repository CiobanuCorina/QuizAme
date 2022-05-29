package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.naming.SizeLimitExceededException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class TimedQuestionnaire extends QuestionnaireDecorator{
    public TimedQuestionnaire(IQuestionnaire wrappee) {
        super(wrappee);
    }

    @Override
    public String ask() throws JsonProcessingException, InterruptedException, SizeLimitExceededException, IllegalAccessException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.awaitTermination(super.getQuestions().size() * 10L, TimeUnit.SECONDS);
        super.ask();
        try {
            Future<Object> perform = service.submit(() -> "TIMEOUT");

            return (String) perform.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }
    }
}
