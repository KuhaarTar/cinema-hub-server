package com.onlinecinema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class MovieRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private LocalDate releaseDate;
    
    @NotNull(message = "Release year is required")
    private Integer releaseYear;

    private String country;
    private String posterUrl;
    private String videoUrl;
    private Integer durationMinutes;
    private Double rating;
    private Set<Long> genreIds;
}

