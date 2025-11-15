package com.onlinecinema.pattern.factory;

import com.onlinecinema.entity.Movie;
import com.onlinecinema.entity.Series;
import org.springframework.stereotype.Component;

@Component
public class ContentFactory {

    public Movie createMovie(String title, String description, Integer releaseYear) {
        return Movie.builder()
                .title(title)
                .description(description)
                .releaseYear(releaseYear)
                .viewCount(0L)
                .build();
    }

    public Series createSeries(String title, String description, Integer releaseYear) {
        return Series.builder()
                .title(title)
                .description(description)
                .releaseYear(releaseYear)
                .viewCount(0L)
                .build();
    }
}

