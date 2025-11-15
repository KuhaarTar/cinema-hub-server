package com.onlinecinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {
    private Long id;
    private Long userId;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Double price;
}

