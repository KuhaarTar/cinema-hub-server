package com.onlinecinema.controller;

import com.onlinecinema.dto.FavoriteDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getMyFavorites() {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId));
    }

    @PostMapping("/movies/{movieId}")
    public ResponseEntity<FavoriteDto> addMovieToFavorites(@PathVariable Long movieId) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addMovieToFavorites(userId, movieId));
    }

    @PostMapping("/series/{seriesId}")
    public ResponseEntity<FavoriteDto> addSeriesToFavorites(@PathVariable Long seriesId) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addSeriesToFavorites(userId, seriesId));
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<Void> removeMovieFromFavorites(@PathVariable Long movieId) {
        Long userId = securityUtils.getCurrentUserId();
        favoriteService.removeMovieFromFavorites(userId, movieId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/series/{seriesId}")
    public ResponseEntity<Void> removeSeriesFromFavorites(@PathVariable Long seriesId) {
        Long userId = securityUtils.getCurrentUserId();
        favoriteService.removeSeriesFromFavorites(userId, seriesId);
        return ResponseEntity.noContent().build();
    }
}

