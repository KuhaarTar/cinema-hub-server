package com.onlinecinema.pattern.observer;

import com.onlinecinema.entity.User;
import com.onlinecinema.entity.ViewingHistory;

public interface RecommendationObserver {
    void update(User user, ViewingHistory viewingHistory);
}

