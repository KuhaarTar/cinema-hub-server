package com.onlinecinema.pattern.strategy;

import com.onlinecinema.entity.Subscription;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SubscriptionStrategyFactory {

    private final Map<Subscription.SubscriptionType, SubscriptionStrategy> strategies;

    public SubscriptionStrategyFactory(
            FreeSubscriptionStrategy freeStrategy,
            PremiumSubscriptionStrategy premiumStrategy) {
        this.strategies = Map.of(
                Subscription.SubscriptionType.FREE, freeStrategy,
                Subscription.SubscriptionType.PREMIUM, premiumStrategy
        );
    }

    public SubscriptionStrategy getStrategy(Subscription.SubscriptionType type) {
        SubscriptionStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown subscription type: " + type);
        }
        return strategy;
    }
}

