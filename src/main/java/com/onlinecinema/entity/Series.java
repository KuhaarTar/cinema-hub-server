package com.onlinecinema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "series")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "release_year")
    private Integer releaseYear;

    private String country;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "series_genres",
            joinColumns = @JoinColumn(name = "series_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();

}

