package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreStorageDao implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;
    private final static String GET_GENRES = "SELECT * FROM GENRE";
    private final static String GET_GENRE_BY_ID = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
    private final static String GENRE_MAPPER = "SELECT G.GENRE_ID, G.NAME FROM GENRE G INNER JOIN FILM_GENRE FG ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?";


    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_GENRES, genreMapper);
    }

    @Override
    public Optional<Genre> getById(int id) {
        try {
            Genre genre = jdbcTemplate.queryForObject(GET_GENRE_BY_ID, genreMapper, id);
            if (genre == null) {
                throw new NotFoundException("Not found");
            }
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found", e);
        }
    }

    @Override
    public void addGenresToFilms(List<Film> films) {

        //   List<Genre> g = jdbcTemplate.query("SELECT FILM_ID, G.GENRE_ID, G.NAME FROM GENRE G INNER JOIN FILM_GENRE FG ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID IN ?", genreMapper, films);

        for (Film film : films) {
            List<Genre> genres = jdbcTemplate.query(GENRE_MAPPER, genreMapper, film.getId());
            LinkedHashSet<Genre> result = new LinkedHashSet<>(genres);
            film.setGenres(result);
        }
    }
}
