package com.onlinecinema.repository;

import com.onlinecinema.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserId(Long userId);
    
    @Query("SELECT s FROM Subscription s WHERE s.isActive = true AND s.type = 'PREMIUM'")
    List<Subscription> findActivePremiumSubscriptions();
    
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.type = 'PREMIUM' AND s.isActive = true")
    Long countActivePremiumSubscriptions();
}

