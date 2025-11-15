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
public class ViewingHistoryDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long seriesId;
    private String seriesTitle;
    private Long episodeId;
    private String episodeTitle;
    private LocalDateTime viewedAt;
    private Long watchTimeSeconds;
    private Boolean completed;
}

