package com.onlinecinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer releaseYear;
    private String country;
    private String posterUrl;
    private Double rating;
    private Long viewCount;
    private Set<String> genres;
}

