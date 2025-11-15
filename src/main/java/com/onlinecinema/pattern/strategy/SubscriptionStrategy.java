package com.onlinecinema.pattern.strategy;

import com.onlinecinema.entity.Subscription;

public interface SubscriptionStrategy {
    boolean canWatchWithoutAds();
    boolean hasAccessToPremiumContent();
    double getPrice();
    String getDescription();
    Subscription.SubscriptionType getType();
}

