package com.onlinecinema.service;

import com.onlinecinema.dto.SubscriptionDto;
import com.onlinecinema.entity.Subscription;
import com.onlinecinema.entity.User;
import com.onlinecinema.pattern.strategy.SubscriptionStrategy;
import com.onlinecinema.pattern.strategy.SubscriptionStrategyFactory;
import com.onlinecinema.repository.SubscriptionRepository;
import com.onlinecinema.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionStrategyFactory strategyFactory;

    public SubscriptionDto findByUserId(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return mapToDto(subscription);
    }

    @Transactional
    public SubscriptionDto upgradeToPremium(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Subscription newSub = Subscription.builder()
                            .user(user)
                            .type(Subscription.SubscriptionType.FREE)
                            .isActive(true)
                            .price(0.0)
                            .build();
                    return subscriptionRepository.save(newSub);
                });

        SubscriptionStrategy strategy = strategyFactory.getStrategy(Subscription.SubscriptionType.PREMIUM);
        
        subscription.setType(Subscription.SubscriptionType.PREMIUM);
        subscription.setPrice(strategy.getPrice());
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(1));
        subscription.setIsActive(true);

        subscription = subscriptionRepository.save(subscription);
        return mapToDto(subscription);
    }

    @Transactional
    public SubscriptionDto downgradeToFree(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Subscription newSub = Subscription.builder()
                            .user(user)
                            .type(Subscription.SubscriptionType.FREE)
                            .isActive(true)
                            .price(0.0)
                            .build();
                    return subscriptionRepository.save(newSub);
                });

        SubscriptionStrategy strategy = strategyFactory.getStrategy(Subscription.SubscriptionType.FREE);
        
        subscription.setType(Subscription.SubscriptionType.FREE);
        subscription.setPrice(strategy.getPrice());
        subscription.setIsActive(true);

        subscription = subscriptionRepository.save(subscription);
        return mapToDto(subscription);
    }

    public boolean canWatchWithoutAds(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElse(null);
        
        if (subscription == null || !subscription.getIsActive()) {
            return false;
        }

        SubscriptionStrategy strategy = strategyFactory.getStrategy(subscription.getType());
        return strategy.canWatchWithoutAds();
    }

    public boolean hasAccessToPremiumContent(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElse(null);
        
        if (subscription == null || !subscription.getIsActive()) {
            return false;
        }

        SubscriptionStrategy strategy = strategyFactory.getStrategy(subscription.getType());
        return strategy.hasAccessToPremiumContent();
    }

    public List<SubscriptionDto> findAll() {
        return subscriptionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubscriptionDto mapToDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .type(subscription.getType().name())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .isActive(subscription.getIsActive())
                .price(subscription.getPrice())
                .build();
    }
}

