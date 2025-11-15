package com.onlinecinema.controller;

import com.onlinecinema.dto.MovieDto;
import com.onlinecinema.dto.MovieRequest;
import com.onlinecinema.dto.ViewingHistoryDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.MovieService;
import com.onlinecinema.service.ViewingHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ViewingHistoryService viewingHistoryService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.search(title));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(movieService.findByGenre(genreId));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<MovieDto>> getMoviesByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(movieService.findByYear(year));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<MovieDto>> getMoviesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(movieService.findByCountry(country));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MovieDto>> getMoviesByFilters(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String country) {
        return ResponseEntity.ok(movieService.findByFilters(title, genre, year, country));
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        return ResponseEntity.ok(movieService.findAllCountries());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<MovieDto>> getPopularMovies(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(movieService.findTopByViewCount(limit));
    }

    @PostMapping("/{id}/watch")
    public ResponseEntity<ViewingHistoryDto> watchMovie(
            @PathVariable Long id,
            @RequestParam(required = false) Long watchTimeSeconds,
            @RequestParam(required = false) Boolean completed) {
        Long userId = securityUtils.getCurrentUserId();
        movieService.incrementViewCount(id);
        ViewingHistoryDto history = viewingHistoryService.recordMovieView(
                userId, id, watchTimeSeconds, completed);
        return ResponseEntity.ok(history);
    }
}

