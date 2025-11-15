package com.onlinecinema.service;

import com.onlinecinema.dto.FavoriteDto;
import com.onlinecinema.entity.Favorite;
import com.onlinecinema.entity.Movie;
import com.onlinecinema.entity.Series;
import com.onlinecinema.entity.User;
import com.onlinecinema.repository.FavoriteRepository;
import com.onlinecinema.repository.MovieRepository;
import com.onlinecinema.repository.SeriesRepository;
import com.onlinecinema.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    @Transactional
    public FavoriteDto addMovieToFavorites(Long userId, Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (favoriteRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new RuntimeException("Movie already in favorites");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .movie(movie)
                .build();

        favorite = favoriteRepository.save(favorite);
        return mapToDto(favorite);
    }

    @Transactional
    public FavoriteDto addSeriesToFavorites(Long userId, Long seriesId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new RuntimeException("Series not found"));

        if (favoriteRepository.existsByUserIdAndSeriesId(userId, seriesId)) {
            throw new RuntimeException("Series already in favorites");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .series(series)
                .build();

        favorite = favoriteRepository.save(favorite);
        return mapToDto(favorite);
    }

    @Transactional
    public void removeMovieFromFavorites(Long userId, Long movieId) {
        Favorite favorite = favoriteRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    @Transactional
    public void removeSeriesFromFavorites(Long userId, Long seriesId) {
        Favorite favorite = favoriteRepository.findByUserIdAndSeriesId(userId, seriesId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    public List<FavoriteDto> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private FavoriteDto mapToDto(Favorite favorite) {
        FavoriteDto.FavoriteDtoBuilder builder = FavoriteDto.builder()
                .id(favorite.getId())
                .addedAt(favorite.getAddedAt());

        if (favorite.getMovie() != null) {
            builder.movieId(favorite.getMovie().getId())
                   .movieTitle(favorite.getMovie().getTitle());
        }

        if (favorite.getSeries() != null) {
            builder.seriesId(favorite.getSeries().getId())
                   .seriesTitle(favorite.getSeries().getTitle());
        }

        return builder.build();
    }
}

