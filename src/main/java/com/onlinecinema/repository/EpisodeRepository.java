package com.onlinecinema.repository;

import com.onlinecinema.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeriesId(Long seriesId);
    
    Optional<Episode> findBySeriesIdAndSeasonAndEpisodeNumber(Long seriesId, Integer season, Integer episodeNumber);
    
    List<Episode> findBySeriesIdAndSeason(Long seriesId, Integer season);
}

