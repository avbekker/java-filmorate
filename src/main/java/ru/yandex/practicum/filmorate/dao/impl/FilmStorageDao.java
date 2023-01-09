package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.interf.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDao implements FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    private final static String CREATE = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)  " +
            "VALUES (?, ?, ?, ?, ?)";
    private final static String SET_LIKE = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
    private final static String DELETE_LIKE = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
    private final static String DELETE = "DELETE FROM FILMS WHERE FILM_ID = ?";
    private final static String UPDATE = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
            "MPA_ID = ? WHERE FILM_ID = ?";
    private final static String GET_FILMS = "SELECT * FROM FILMS" +
            " LEFT JOIN MPA M on FILMS.MPA_ID = M.MPA_ID" +
            " LEFT JOIN FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID";
    private final static String GET_FILM_BY_ID = "SELECT * FROM FILMS WHERE FILM_ID = ?";
    private final static String DELETE_GENRE_FROM_FILM = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
    private final static String GENRE_TO_FILM = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";


    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE, new String[] {"FILM_ID"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                ps.setInt(5, film.getMpa().getId());
                return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);
        genreToFilm(id, film.getGenres());
        return getById(id).orElseThrow(() -> new NotFoundException("Not found"));
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.update(DELETE,film.getId());
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(UPDATE, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        genreToFilm(film.getId(), film.getGenres());
        return getById(film.getId()).orElseThrow(() -> new NotFoundException("Not Found"));
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(GET_FILMS, filmMapper);
    }

    @Override
    public Optional<Film> getById(long id) {
        try {
            Film film = jdbcTemplate.queryForObject(GET_FILM_BY_ID, filmMapper, id);
            if (film == null) {
                throw new NotFoundException("Not found");
            }
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found", e);
        }
    }

    @Override
    public void setLike(Long userId, Film film) {
        film.getLikes().add(userId);
        jdbcTemplate.update(SET_LIKE, film.getId(), userId);
    }

    @Override
    public void deleteLike(Long userId, Film film) {
        film.getLikes().remove(userId);
        jdbcTemplate.update(DELETE_LIKE, film.getId());
    }

    private void genreToFilm(long filmId, List<Genre> genres) {
        jdbcTemplate.update(DELETE_GENRE_FROM_FILM, filmId);
        if (genres == null) {
            return;
        }
        Set<Genre> genresWithoutDouble = new LinkedHashSet<>(genres);
        for (Genre genre : genresWithoutDouble) {
            jdbcTemplate.update(GENRE_TO_FILM, filmId, genre.getId());
        }
    }
}
