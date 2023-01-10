package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreStorageDao implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final static String GET_GENRES = "SELECT * FROM GENRE";
    private final static String GET_GENRE_BY_ID = "SELECT * FROM GENRE WHERE GENRE_ID = ?";

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_GENRES, new GenreMapper());
    }

    @Override
    public Optional<Genre> getById(int id) {
        try {
            Genre genre = jdbcTemplate.queryForObject(GET_GENRE_BY_ID, new GenreMapper(), id);
            if (genre == null) {
                throw new NotFoundException("Not found");
            }
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found", e);
        }
    }
}
