package com.onlinecinema.service;

import com.onlinecinema.dto.MovieDto;
import com.onlinecinema.dto.MovieRequest;
import com.onlinecinema.entity.Genre;
import com.onlinecinema.entity.Movie;
import com.onlinecinema.pattern.factory.ContentFactory;
import com.onlinecinema.repository.GenreRepository;
import com.onlinecinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ContentFactory contentFactory;

    @Transactional(readOnly = true)
    public List<MovieDto> findAll() {
        return movieRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieDto findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDto(movie);
    }

    @Transactional(readOnly = true)
    public List<MovieDto> search(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findByGenre(Long genreId) {
        return movieRepository.findByGenreId(genreId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findByYear(Integer year) {
        return movieRepository.findByReleaseYear(year).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findByCountry(String country) {
        return movieRepository.findByCountry(country).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findTopByViewCount(int limit) {
        return movieRepository.findTopByViewCount().stream()
                .limit(limit)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findByFilters(String title, Long genreId, Integer year, String country) {
        return movieRepository.findByFilters(title, genreId, year, country).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> findAllCountries() {
        return movieRepository.findDistinctCountries();
    }

    @Transactional
    public MovieDto create(MovieRequest request) {
        Movie movie = contentFactory.createMovie(
                request.getTitle(),
                request.getDescription(),
                request.getReleaseYear()
        );

        movie.setReleaseDate(request.getReleaseDate());
        movie.setCountry(request.getCountry());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setVideoUrl(request.getVideoUrl());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setRating(request.getRating());

        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<Genre> genres = request.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + genreId)))
                    .collect(Collectors.toSet());
            movie.setGenres(genres);
        }

        movie = movieRepository.save(movie);
        return mapToDto(movie);
    }

    @Transactional
    public MovieDto update(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setCountry(request.getCountry());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setVideoUrl(request.getVideoUrl());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setRating(request.getRating());

        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<Genre> genres = request.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + genreId)))
                    .collect(Collectors.toSet());
            movie.setGenres(genres);
        }

        movie = movieRepository.save(movie);
        return mapToDto(movie);
    }

    @Transactional
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setViewCount(movie.getViewCount() + 1);
        movieRepository.save(movie);
    }

    private MovieDto mapToDto(Movie movie) {
        Set<String> genreNames = movie.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());

        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .releaseYear(movie.getReleaseYear())
                .country(movie.getCountry())
                .posterUrl(movie.getPosterUrl())
                .videoUrl(movie.getVideoUrl())
                .durationMinutes(movie.getDurationMinutes())
                .rating(movie.getRating())
                .viewCount(movie.getViewCount())
                .genres(genreNames)
                .build();
    }
}

