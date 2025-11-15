package com.onlinecinema.repository;

import com.onlinecinema.entity.Series;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    
    @EntityGraph(attributePaths = {"genres"})
    @Override
    List<Series> findAll();
    
    @EntityGraph(attributePaths = {"genres"})
    @Override
    Optional<Series> findById(Long id);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.genres WHERE s.title LIKE %:title%")
    List<Series> findByTitleContainingIgnoreCase(@Param("title") String title);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.genres JOIN s.genres g WHERE g.id = :genreId")
    List<Series> findByGenreId(@Param("genreId") Long genreId);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.genres WHERE s.releaseYear = :year")
    List<Series> findByReleaseYear(@Param("year") Integer year);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.genres WHERE s.country = :country")
    List<Series> findByCountry(@Param("country") String country);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.genres ORDER BY s.viewCount DESC")
    List<Series> findTopByViewCount();
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT s FROM Series s LEFT JOIN FETCH s.genres ORDER BY s.rating DESC")
    List<Series> findTopByRating();
}

