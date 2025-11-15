package com.onlinecinema.pattern.strategy;

import com.onlinecinema.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class PremiumSubscriptionStrategy implements SubscriptionStrategy {

    private static final double PREMIUM_PRICE = 9.99;

    @Override
    public boolean canWatchWithoutAds() {
        return true;
    }

    @Override
    public boolean hasAccessToPremiumContent() {
        return true;
    }

    @Override
    public double getPrice() {
        return PREMIUM_PRICE;
    }

    @Override
    public String getDescription() {
        return "Premium subscription without ads";
    }

    @Override
    public Subscription.SubscriptionType getType() {
        return Subscription.SubscriptionType.PREMIUM;
    }
}

