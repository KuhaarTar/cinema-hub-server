package com.onlinecinema.service;

import com.onlinecinema.dto.SeriesDto;
import com.onlinecinema.dto.SeriesRequest;
import com.onlinecinema.entity.Genre;
import com.onlinecinema.entity.Series;
import com.onlinecinema.pattern.factory.ContentFactory;
import com.onlinecinema.repository.GenreRepository;
import com.onlinecinema.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;
    private final ContentFactory contentFactory;

    @Transactional(readOnly = true)
    public List<SeriesDto> findAll() {
        return seriesRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SeriesDto findById(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found"));
        return mapToDto(series);
    }

    @Transactional(readOnly = true)
    public List<SeriesDto> search(String title) {
        return seriesRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SeriesDto> findByGenre(Long genreId) {
        return seriesRepository.findByGenreId(genreId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SeriesDto> findByYear(Integer year) {
        return seriesRepository.findByReleaseYear(year).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SeriesDto> findByCountry(String country) {
        return seriesRepository.findByCountry(country).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SeriesDto create(SeriesRequest request) {
        Series series = contentFactory.createSeries(
                request.getTitle(),
                request.getDescription(),
                request.getReleaseYear()
        );

        series.setReleaseDate(request.getReleaseDate());
        series.setCountry(request.getCountry());
        series.setPosterUrl(request.getPosterUrl());
        series.setRating(request.getRating());

        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<Genre> genres = request.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + genreId)))
                    .collect(Collectors.toSet());
            series.setGenres(genres);
        }

        series = seriesRepository.save(series);
        return mapToDto(series);
    }

    @Transactional
    public SeriesDto update(Long id, SeriesRequest request) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found"));

        series.setTitle(request.getTitle());
        series.setDescription(request.getDescription());
        series.setReleaseDate(request.getReleaseDate());
        series.setReleaseYear(request.getReleaseYear());
        series.setCountry(request.getCountry());
        series.setPosterUrl(request.getPosterUrl());
        series.setRating(request.getRating());

        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<Genre> genres = request.getGenreIds().stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + genreId)))
                    .collect(Collectors.toSet());
            series.setGenres(genres);
        }

        series = seriesRepository.save(series);
        return mapToDto(series);
    }

    @Transactional
    public void deleteById(Long id) {
        seriesRepository.deleteById(id);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found"));
        series.setViewCount(series.getViewCount() + 1);
        seriesRepository.save(series);
    }

    private SeriesDto mapToDto(Series series) {
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

