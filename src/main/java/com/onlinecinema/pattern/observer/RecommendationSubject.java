package com.onlinecinema.pattern.observer;

import com.onlinecinema.entity.User;
import com.onlinecinema.entity.ViewingHistory;

import java.util.ArrayList;
import java.util.List;

public class RecommendationSubject {
    private final List<RecommendationObserver> observers = new ArrayList<>();

    public void attach(RecommendationObserver observer) {
        observers.add(observer);
    }

    public void detach(RecommendationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(User user, ViewingHistory viewingHistory) {
        observers.forEach(observer -> observer.update(user, viewingHistory));
    }
}

