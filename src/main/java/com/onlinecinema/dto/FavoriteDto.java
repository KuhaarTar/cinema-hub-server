package com.onlinecinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long seriesId;
    private String seriesTitle;
    private LocalDateTime addedAt;
}

