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
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
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
    private final static String DELETE = "DELETE FROM FILMS WHERE FILM_ID = ?";
    private final static String UPDATE = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
            "MPA_ID = ? WHERE FILM_ID = ?";
    private final static String GET_FILMS = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION," +
            " F.MPA_ID, M.NAME AS MPA_NAME  FROM FILMS F" +
            " LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID" +
            " LEFT JOIN FILM_GENRE FG on F.FILM_ID = FG.FILM_ID";
    private final static String GET_FILM_BY_ID = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION," +
            " F.MPA_ID, M.NAME AS MPA_NAME FROM FILMS F" +
            " LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID" +
            " WHERE FILM_ID = ?";
    private final static String GENRE_MAPPER = "SELECT G.GENRE_ID, G.NAME FROM GENRE G INNER JOIN FILM_GENRE FG ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?";
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
        genreToFilm(film, film.getGenres());
        return film;
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.update(DELETE,film.getId());
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(UPDATE, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        genreToFilm(film, film.getGenres());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query(GET_FILMS, filmMapper);
        for (Film film : films) {
            addGenres(film, film.getId());
        }
        return films;
    }

    @Override
    public Optional<Film> getById(long id) {
        try {
            Film film = jdbcTemplate.queryForObject(GET_FILM_BY_ID, filmMapper, id);
            if (film == null) {
                throw new NotFoundException("Not found");
            }
            addGenres(film, id);
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found", e);
        }
    }

    private void genreToFilm(Film film, List<Genre> genres) {
        jdbcTemplate.update(DELETE_GENRE_FROM_FILM, film.getId());
        if (genres == null || genres.isEmpty()) {
            return;
        }
        Set<Genre> genreSet = new LinkedHashSet<>(genres);
        film.setGenres(new ArrayList<>(genreSet));
        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : genreSet) {
            Long[] values = new Long[]{film.getId(), (long) genre.getId()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(GENRE_TO_FILM, batch);
    }
    
    public void addGenres(Film film, long filmId){
        List<Genre> genres = jdbcTemplate.query(GENRE_MAPPER, new GenreMapper(), filmId);
        film.setGenres(genres);
    }
}
