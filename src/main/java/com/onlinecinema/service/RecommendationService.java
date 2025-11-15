package com.onlinecinema.service;

import com.onlinecinema.dto.MovieDto;
import com.onlinecinema.dto.SeriesDto;
import com.onlinecinema.entity.Genre;
import com.onlinecinema.entity.User;
import com.onlinecinema.entity.ViewingHistory;
import com.onlinecinema.pattern.observer.GenreBasedRecommendationObserver;
import com.onlinecinema.pattern.observer.RecommendationSubject;
import com.onlinecinema.repository.GenreRepository;
import com.onlinecinema.repository.MovieRepository;
import com.onlinecinema.repository.SeriesRepository;
import com.onlinecinema.repository.UserRepository;
import com.onlinecinema.repository.ViewingHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final ViewingHistoryRepository viewingHistoryRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;
    private final GenreBasedRecommendationObserver recommendationObserver;
    private final RecommendationSubject recommendationSubject;

    public RecommendationService(
            UserRepository userRepository,
            ViewingHistoryRepository viewingHistoryRepository,
            MovieRepository movieRepository,
            SeriesRepository seriesRepository,
            GenreRepository genreRepository,
            GenreBasedRecommendationObserver recommendationObserver) {
        this.userRepository = userRepository;
        this.viewingHistoryRepository = viewingHistoryRepository;
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
        this.genreRepository = genreRepository;
        this.recommendationObserver = recommendationObserver;
        this.recommendationSubject = new RecommendationSubject();
        this.recommendationSubject.attach(recommendationObserver);
    }

    public List<MovieDto> recommendMovies(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ViewingHistory> history = viewingHistoryRepository.findMovieHistoryByUserId(userId);
        
        if (history.isEmpty()) {
            return movieRepository.findTopByViewCount().stream()
                    .limit(10)
                    .map(this::mapToMovieDto)
                    .collect(Collectors.toList());
        }

        Map<Long, Long> genreFrequency = new HashMap<>();
        for (ViewingHistory vh : history) {
            if (vh.getMovie() != null) {
                for (Genre genre : vh.getMovie().getGenres()) {
                    genreFrequency.put(genre.getId(), genreFrequency.getOrDefault(genre.getId(), 0L) + 1);
                }
            }
        }

        List<Long> topGenres = genreFrequency.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Set<Long> viewedMovieIds = history.stream()
                .map(vh -> vh.getMovie().getId())
                .collect(Collectors.toSet());

        List<com.onlinecinema.entity.Movie> recommendations = new ArrayList<>();
        for (Long genreId : topGenres) {
            List<com.onlinecinema.entity.Movie> movies = movieRepository.findByGenreId(genreId);
            recommendations.addAll(movies.stream()
                    .filter(m -> !viewedMovieIds.contains(m.getId()))
                    .limit(5)
                    .collect(Collectors.toList()));
        }

        return recommendations.stream()
                .map(this::mapToMovieDto)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<SeriesDto> recommendSeries(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ViewingHistory> history = viewingHistoryRepository.findSeriesHistoryByUserId(userId);
        
        if (history.isEmpty()) {
            return seriesRepository.findTopByViewCount().stream()
                    .limit(10)
                    .map(this::mapToSeriesDto)
                    .collect(Collectors.toList());
        }

        Map<Long, Long> genreFrequency = new HashMap<>();
        for (ViewingHistory vh : history) {
            if (vh.getSeries() != null) {
                for (Genre genre : vh.getSeries().getGenres()) {
                    genreFrequency.put(genre.getId(), genreFrequency.getOrDefault(genre.getId(), 0L) + 1);
                }
            }
        }

        List<Long> topGenres = genreFrequency.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Set<Long> viewedSeriesIds = history.stream()
                .map(vh -> vh.getSeries().getId())
                .collect(Collectors.toSet());

        List<com.onlinecinema.entity.Series> recommendations = new ArrayList<>();
        for (Long genreId : topGenres) {
            List<com.onlinecinema.entity.Series> series = seriesRepository.findByGenreId(genreId);
            recommendations.addAll(series.stream()
                    .filter(s -> !viewedSeriesIds.contains(s.getId()))
                    .limit(5)
                    .collect(Collectors.toList()));
        }

        return recommendations.stream()
                .map(this::mapToSeriesDto)
                .limit(10)
                .collect(Collectors.toList());
    }

    public void notifyViewing(User user, ViewingHistory viewingHistory) {
        recommendationSubject.notifyObservers(user, viewingHistory);
    }

    private MovieDto mapToMovieDto(com.onlinecinema.entity.Movie movie) {
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

    private SeriesDto mapToSeriesDto(com.onlinecinema.entity.Series series) {
        Set<String> genreNames = series.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());

        return SeriesDto.builder()
                .id(series.getId())
                .title(series.getTitle())
                .description(series.getDescription())
                .releaseDate(series.getReleaseDate())
                .releaseYear(series.getReleaseYear())
                .country(series.getCountry())
                .posterUrl(series.getPosterUrl())
                .rating(series.getRating())
                .viewCount(series.getViewCount())
                .genres(genreNames)
                .build();
    }
}

