package com.onlinecinema.controller;

import com.onlinecinema.dto.SeriesDto;
import com.onlinecinema.dto.ViewingHistoryDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.SeriesService;
import com.onlinecinema.service.ViewingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;
    private final ViewingHistoryService viewingHistoryService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<List<SeriesDto>> getAllSeries() {
        return ResponseEntity.ok(seriesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeriesDto> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SeriesDto>> searchSeries(@RequestParam String title) {
        return ResponseEntity.ok(seriesService.search(title));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<SeriesDto>> getSeriesByGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(seriesService.findByGenre(genreId));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<SeriesDto>> getSeriesByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(seriesService.findByYear(year));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<SeriesDto>> getSeriesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(seriesService.findByCountry(country));
    }

    @PostMapping("/{id}/episodes/{episodeId}/watch")
    public ResponseEntity<ViewingHistoryDto> watchSeries(
            @PathVariable Long id,
            @PathVariable Long episodeId,
            @RequestParam(required = false) Long watchTimeSeconds,
            @RequestParam(required = false) Boolean completed) {
        Long userId = securityUtils.getCurrentUserId();
        seriesService.incrementViewCount(id);
        ViewingHistoryDto history = viewingHistoryService.recordSeriesView(
                userId, id, episodeId, watchTimeSeconds, completed);
        return ResponseEntity.ok(history);
    }
}

