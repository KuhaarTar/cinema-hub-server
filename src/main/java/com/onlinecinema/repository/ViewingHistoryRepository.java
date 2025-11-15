package com.onlinecinema.repository;

import com.onlinecinema.entity.ViewingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewingHistoryRepository extends JpaRepository<ViewingHistory, Long> {
    List<ViewingHistory> findByUserIdOrderByViewedAtDesc(Long userId);
    
    @Query("SELECT vh FROM ViewingHistory vh WHERE vh.user.id = :userId AND vh.movie IS NOT NULL")
    List<ViewingHistory> findMovieHistoryByUserId(@Param("userId") Long userId);
    
    @Query("SELECT vh FROM ViewingHistory vh WHERE vh.user.id = :userId AND vh.series IS NOT NULL")
    List<ViewingHistory> findSeriesHistoryByUserId(@Param("userId") Long userId);
}

