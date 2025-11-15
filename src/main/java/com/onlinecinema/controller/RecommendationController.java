package com.onlinecinema.controller;

import com.onlinecinema.dto.MovieDto;
import com.onlinecinema.dto.SeriesDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final SecurityUtils securityUtils;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getMovieRecommendations() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(recommendationService.recommendMovies(userId));
    }

    @GetMapping("/series")
    public ResponseEntity<List<SeriesDto>> getSeriesRecommendations() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(recommendationService.recommendSeries(userId));
    }
}

