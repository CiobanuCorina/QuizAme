package com.company;

import com.company.User.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {
    private HashMap<String, List<Subscriber>> listeners = new HashMap<>();
    List<Subscriber> subscribers;

    public void subscribe(String event, Subscriber subscriber) {
        if(listeners.containsKey(event)) subscribers = listeners.get(event);
        else subscribers = new ArrayList<>();
        subscribers.add(subscriber);
        listeners.put(event, subscribers);
    }

    public void unsubscribe(String event, Subscriber subscriber) {
        subscribers = listeners.get(event);
        subscribers.remove(subscriber);
        if(subscribers.isEmpty()) listeners.remove(event);
    }

    public void notifyListeners(String event, User user) throws IOException {
        for (Subscriber subscriber : listeners.get(event)) {
            subscriber.update(user);
        }
    }
}
