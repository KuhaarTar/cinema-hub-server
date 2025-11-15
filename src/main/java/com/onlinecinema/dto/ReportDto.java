package com.onlinecinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    private Map<String, Long> popularMovies;
    private Map<String, Long> popularGenres;
    private Long activeUsers;
    private Long totalUsers;
    private Double subscriptionRevenue;
    private Long premiumSubscriptions;
}

