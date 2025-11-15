package com.onlinecinema.controller;

import com.onlinecinema.dto.SubscriptionDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SecurityUtils securityUtils;

    @GetMapping("/me")
    public ResponseEntity<SubscriptionDto> getMySubscription() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(subscriptionService.findByUserId(userId));
    }

    @PostMapping("/upgrade")
    public ResponseEntity<SubscriptionDto> upgradeToPremium() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(subscriptionService.upgradeToPremium(userId));
    }

    @PostMapping("/downgrade")
    public ResponseEntity<SubscriptionDto> downgradeToFree() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(subscriptionService.downgradeToFree(userId));
    }
}

