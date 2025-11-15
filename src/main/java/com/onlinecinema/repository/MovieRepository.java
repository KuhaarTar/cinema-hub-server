package com.onlinecinema.repository;

import com.onlinecinema.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    @EntityGraph(attributePaths = {"genres"})
    @Override
    List<Movie> findAll();
    
    @EntityGraph(attributePaths = {"genres"})
    @Override
    Optional<Movie> findById(Long id);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres WHERE m.title LIKE %:title%")
    List<Movie> findByTitleContainingIgnoreCase(@Param("title") String title);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres JOIN m.genres g WHERE g.id = :genreId")
    List<Movie> findByGenreId(@Param("genreId") Long genreId);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres WHERE m.releaseYear = :year")
    List<Movie> findByReleaseYear(@Param("year") Integer year);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres WHERE m.country = :country")
    List<Movie> findByCountry(@Param("country") String country);
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres ORDER BY m.viewCount DESC")
    List<Movie> findTopByViewCount();
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres ORDER BY m.rating DESC")
    List<Movie> findTopByRating();
    
    @EntityGraph(attributePaths = {"genres"})
    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres " +
           "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:genreId IS NULL OR EXISTS (SELECT 1 FROM m.genres g WHERE g.id = :genreId)) " +
           "AND (:year IS NULL OR m.releaseYear = :year) " +
           "AND (:country IS NULL OR m.country = :country)")
    List<Movie> findByFilters(
            @Param("title") String title,
            @Param("genreId") Long genreId,
            @Param("year") Integer year,
            @Param("country") String country
    );
    
    @Query("SELECT DISTINCT m.country FROM Movie m WHERE m.country IS NOT NULL ORDER BY m.country")
    List<String> findDistinctCountries();
}

