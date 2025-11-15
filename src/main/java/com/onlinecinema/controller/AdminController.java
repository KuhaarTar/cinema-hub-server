package com.onlinecinema.controller;

import com.onlinecinema.dto.*;
import com.onlinecinema.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;
    private final SeriesService seriesService;
    private final GenreService genreService;
    private final UserService userService;

    @PostMapping("/movies")
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.create(request));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.update(id, request));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/series")
    public ResponseEntity<SeriesDto> createSeries(@Valid @RequestBody SeriesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesService.create(request));
    }

    @PutMapping("/series/{id}")
    public ResponseEntity<SeriesDto> updateSeries(@PathVariable Long id, @Valid @RequestBody SeriesRequest request) {
        return ResponseEntity.ok(seriesService.update(id, request));
    }

    @DeleteMapping("/series/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        seriesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @PostMapping("/genres")
    public ResponseEntity<GenreDto> createGenre(@RequestParam String name, @RequestParam(required = false) String description) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(name, description));
    }

    @PutMapping("/genres/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String description) {
        return ResponseEntity.ok(genreService.update(id, name, description));
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

