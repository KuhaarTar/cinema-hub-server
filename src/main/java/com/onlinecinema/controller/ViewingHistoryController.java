package com.onlinecinema.controller;

import com.onlinecinema.dto.ViewingHistoryDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.ViewingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viewing-history")
@RequiredArgsConstructor
public class ViewingHistoryController {

    private final ViewingHistoryService viewingHistoryService;
    private final SecurityUtils securityUtils;

    @GetMapping("/me")
    public ResponseEntity<List<ViewingHistoryDto>> getMyViewingHistory() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(viewingHistoryService.getUserHistory(userId));
    }

    @PostMapping("/movies/{movieId}")
    public ResponseEntity<ViewingHistoryDto> recordMovieView(
            @PathVariable Long movieId,
            @RequestParam(required = false) Long watchTimeSeconds,
            @RequestParam(required = false) Boolean completed) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(viewingHistoryService.recordMovieView(
                userId, movieId, watchTimeSeconds, completed));
    }

    @PostMapping("/series/{seriesId}/episodes/{episodeId}")
    public ResponseEntity<ViewingHistoryDto> recordSeriesView(
            @PathVariable Long seriesId,
            @PathVariable Long episodeId,
            @RequestParam(required = false) Long watchTimeSeconds,
            @RequestParam(required = false) Boolean completed) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(viewingHistoryService.recordSeriesView(
                userId, seriesId, episodeId, watchTimeSeconds, completed));
    }
}

