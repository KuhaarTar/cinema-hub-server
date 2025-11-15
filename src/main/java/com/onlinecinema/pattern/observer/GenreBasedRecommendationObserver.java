package com.onlinecinema.pattern.observer;

import com.onlinecinema.entity.User;
import com.onlinecinema.entity.ViewingHistory;
import org.springframework.stereotype.Component;

@Component
public class GenreBasedRecommendationObserver implements RecommendationObserver {

    @Override
    public void update(User user, ViewingHistory viewingHistory) {
        if (viewingHistory.getMovie() != null) {
            System.out.println("User " + user.getUsername() + " watched a movie, updating genre preferences");
        } else if (viewingHistory.getSeries() != null) {
            System.out.println("User " + user.getUsername() + " watched a series, updating genre preferences");
        }
    }
}

