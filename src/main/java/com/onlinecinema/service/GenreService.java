package com.onlinecinema.service;

import com.onlinecinema.dto.GenreDto;
import com.onlinecinema.entity.Genre;
import com.onlinecinema.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public GenreDto findById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        return mapToDto(genre);
    }

    @Transactional
    public GenreDto create(String name, String description) {
        if (genreRepository.existsByName(name)) {
            throw new RuntimeException("Genre already exists");
        }

        Genre genre = new Genre();
        genre.setName(name);
        genre.setDescription(description);
        genre = genreRepository.save(genre);
        return mapToDto(genre);
    }

    @Transactional
    public GenreDto update(Long id, String name, String description) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genre.setName(name);
        genre.setDescription(description);
        genre = genreRepository.save(genre);
        return mapToDto(genre);
    }

    @Transactional
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }

    private GenreDto mapToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName(), genre.getDescription());
    }
}

