package com.onlinecinema.service;

import com.onlinecinema.dto.ReportDto;
import com.onlinecinema.entity.Genre;
import com.onlinecinema.entity.Movie;
import com.onlinecinema.entity.Series;
import com.onlinecinema.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating reports using Template Method pattern
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ViewingHistoryRepository viewingHistoryRepository;

    public ReportDto generateReport() {
        ReportDto.ReportDtoBuilder builder = ReportDto.builder();

        Map<String, Long> popularMovies = getPopularMovies();
        builder.popularMovies(popularMovies);

        Map<String, Long> popularGenres = getPopularGenres();
        builder.popularGenres(popularGenres);

        Long activeUsers = getActiveUsersCount();
        Long totalUsers = userRepository.count();
        builder.activeUsers(activeUsers);
        builder.totalUsers(totalUsers);

        Double revenue = calculateSubscriptionRevenue();
        Long premiumSubscriptions = subscriptionRepository.countActivePremiumSubscriptions();
        builder.subscriptionRevenue(revenue);
        builder.premiumSubscriptions(premiumSubscriptions);

        return builder.build();
    }

    private Map<String, Long> getPopularMovies() {
        List<Movie> topMovies = movieRepository.findTopByViewCount().stream()
                .limit(10)
                .collect(Collectors.toList());

        Map<String, Long> result = new LinkedHashMap<>();
        for (Movie movie : topMovies) {
            result.put(movie.getTitle(), movie.getViewCount());
        }
        return result;
    }

    private Map<String, Long> getPopularGenres() {
        Map<String, Long> genreCounts = new HashMap<>();

        for (Movie movie : movieRepository.findAll()) {
            for (Genre genre : movie.getGenres()) {
                genreCounts.put(genre.getName(), 
                    genreCounts.getOrDefault(genre.getName(), 0L) + movie.getViewCount());
            }
        }

        for (Series series : seriesRepository.findAll()) {
            for (Genre genre : series.getGenres()) {
                genreCounts.put(genre.getName(), 
                    genreCounts.getOrDefault(genre.getName(), 0L) + series.getViewCount());
            }
        }

        return genreCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Long getActiveUsersCount() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo = cal.getTime();

        return viewingHistoryRepository.findAll().stream()
                .filter(vh -> vh.getViewedAt().isAfter(
                    java.time.LocalDateTime.ofInstant(thirtyDaysAgo.toInstant(), 
                    java.time.ZoneId.systemDefault())))
                .map(vh -> vh.getUser().getId())
                .distinct()
                .count();
    }

    private Double calculateSubscriptionRevenue() {
        return subscriptionRepository.findActivePremiumSubscriptions().stream()
                .mapToDouble(sub -> sub.getPrice() != null ? sub.getPrice() : 0.0)
                .sum();
    }
}

