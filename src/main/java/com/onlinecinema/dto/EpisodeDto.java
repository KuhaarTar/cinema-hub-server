package com.onlinecinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeDto {
    private Long id;
    private Long seriesId;
    private Integer season;
    private Integer episodeNumber;
    private String title;
    private String description;
    private String videoUrl;
    private Integer durationMinutes;
    private Long viewCount;
}

