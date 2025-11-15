package com.onlinecinema.service;

import com.onlinecinema.dto.ViewingHistoryDto;
import com.onlinecinema.entity.*;
import com.onlinecinema.repository.*;
import com.onlinecinema.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewingHistoryService {

    private final ViewingHistoryRepository viewingHistoryRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final EpisodeRepository episodeRepository;
    private final RecommendationService recommendationService;

    @Transactional
    public ViewingHistoryDto recordMovieView(Long userId, Long movieId, Long watchTimeSeconds, Boolean completed) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        ViewingHistory history = ViewingHistory.builder()
                .user(user)
                .movie(movie)
                .watchTimeSeconds(watchTimeSeconds)
                .completed(completed != null ? completed : false)
                .build();

        history = viewingHistoryRepository.save(history);
        recommendationService.notifyViewing(user, history);
        
        return mapToDto(history);
    }

    @Transactional
    public ViewingHistoryDto recordSeriesView(Long userId, Long seriesId, Long episodeId, Long watchTimeSeconds, Boolean completed) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new RuntimeException("Series not found"));
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new RuntimeException("Episode not found"));

        ViewingHistory history = ViewingHistory.builder()
                .user(user)
                .series(series)
                .episode(episode)
                .watchTimeSeconds(watchTimeSeconds)
                .completed(completed != null ? completed : false)
                .build();

        history = viewingHistoryRepository.save(history);
        recommendationService.notifyViewing(user, history);
        
        return mapToDto(history);
    }

    public List<ViewingHistoryDto> getUserHistory(Long userId) {
        return viewingHistoryRepository.findByUserIdOrderByViewedAtDesc(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ViewingHistoryDto mapToDto(ViewingHistory history) {
        ViewingHistoryDto.ViewingHistoryDtoBuilder builder = ViewingHistoryDto.builder()
                .id(history.getId())
                .viewedAt(history.getViewedAt())
                .watchTimeSeconds(history.getWatchTimeSeconds())
                .completed(history.getCompleted());

        if (history.getMovie() != null) {
            builder.movieId(history.getMovie().getId())
                   .movieTitle(history.getMovie().getTitle());
        }

        if (history.getSeries() != null) {
            builder.seriesId(history.getSeries().getId())
                   .seriesTitle(history.getSeries().getTitle());
        }

        if (history.getEpisode() != null) {
            builder.episodeId(history.getEpisode().getId())
                   .episodeTitle(history.getEpisode().getTitle());
        }

        return builder.build();
    }
}

