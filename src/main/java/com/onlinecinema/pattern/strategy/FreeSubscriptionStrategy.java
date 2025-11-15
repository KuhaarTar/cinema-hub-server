package com.onlinecinema.pattern.strategy;

import com.onlinecinema.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class FreeSubscriptionStrategy implements SubscriptionStrategy {

    @Override
    public boolean canWatchWithoutAds() {
        return false;
    }

    @Override
    public boolean hasAccessToPremiumContent() {
        return false;
    }

    @Override
    public double getPrice() {
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "Free subscription with ads";
    }

    @Override
    public Subscription.SubscriptionType getType() {
        return Subscription.SubscriptionType.FREE;
    }
}

