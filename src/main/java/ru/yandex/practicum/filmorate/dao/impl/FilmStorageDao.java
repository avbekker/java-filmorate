package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.interf.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDao implements FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    private final static String CREATE = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)  " +
            "VALUES (?, ?, ?, ?, ?)";
    private final static String GET_LAST_ADDED = "SELECT FILM_ID FROM FILMS ORDER BY FILM_ID DESC LIMIT 1";
    private final static String DELETE = "DELETE FROM FILMS WHERE FILM_ID = ?";
    private final static String UPDATE = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
            "MPA_ID = ? WHERE FILM_ID = ?";
    private final static String GET_FILMS = "SELECT * FROM FILMS" +
            " LEFT JOIN MPA M on FILMS.MPA_ID = M.MPA_ID" +
            " LEFT JOIN FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID";
    private final static String GET_FILM_BY_ID = "SELECT * FROM FILMS WHERE FILM_ID = ?";


    @Override
    public Film create(Film film) {
        jdbcTemplate.update(CREATE, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        Integer id = jdbcTemplate.queryForObject(GET_LAST_ADDED, Integer.class);
        if (id == null) {
            throw new NotFoundException("Not found");
        }
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
        return jdbcTemplate.query(GET_FILMS, filmMapper, null);
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
        log.info("Пользователь c id {} поставил лайк фильму {}", userId, film.getName());
        film.getLikes().add(userId);
        jdbcTemplate.update("INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)", film.getId(), userId);
    }

    @Override
    public void deleteLike(Long userId, Film film) {
        log.info("Пользователь c id {} удалил лайк фильму {}", userId, film.getName());
        film.getLikes().remove(userId);
        jdbcTemplate.update("DELETE FROM FILM_LIKES WHERE FILM_ID = ?", film.getId());
    }

    private void genreToFilm(long filmId, List<Genre> genres) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", filmId);
        if (genres == null) {
            return;
        }
        Set<Genre> genresWithoutDouble = new LinkedHashSet<>(genres);
        for (Genre genre : genresWithoutDouble) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)", filmId, genre.getId());
        }
    }

    public List<Genre> getGenres() {
        List<Genre> result = new ArrayList<>();
        List<Integer> genreIds = jdbcTemplate.queryForList("SELECT GENRE_ID FROM GENRE", Integer.class);
        for (Integer genreId : genreIds) {
            result.add(filmMapper.genreMapper(genreId));
        }
        return result;
    }

    public Optional<Genre> getGenreById(int id) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?", id);
        if (genreRow.next()) {
            Genre genre = Genre.builder()
                    .id(genreRow.getInt("GENRE_ID"))
                    .name(genreRow.getString("NAME"))
                    .build();
            log.info("Найден жанр: {}.", genre.getName());
            return Optional.of(genre);
        }
        log.info("Жанр с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    public List<MPA> getMPAs() {
        List<MPA> result = new ArrayList<>();
        List<Integer> mpaIds = jdbcTemplate.queryForList("SELECT MPA_ID FROM MPA", Integer.class);
        for (Integer mpaId : mpaIds) {
            result.add(filmMapper.mpaMapper(mpaId));
        }
        return result;
    }

    public Optional<MPA> getMPAById(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ?", id);
        if (mpaRow.next()) {
            MPA mpa = MPA.builder()
                    .id(mpaRow.getInt("MPA_ID"))
                    .name(mpaRow.getString("NAME"))
                    .build();
            log.info("Найден рейтинг: {}.", mpa.getName());
            return Optional.of(mpa);
        }
        log.info("Рейтинг с идентификатором {} не найден.", id);
        return Optional.empty();
    }
}
