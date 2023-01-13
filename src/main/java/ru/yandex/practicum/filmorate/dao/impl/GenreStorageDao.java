package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreStorageDao implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;
    private final static String GET_GENRES = "SELECT * FROM GENRE";
    private final static String GET_GENRE_BY_ID = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
    private final static String GENRE_MAPPER = "SELECT FG.FILM_ID, G.GENRE_ID, G.NAME FROM GENRE G INNER JOIN FILM_GENRE FG " +
            "ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID IN (%s) ORDER BY GENRE_ORDER";


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
        final Map<Long, Film> filmIds = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        List<Long> ids = new ArrayList<>();
        for (Film film : films) {
            ids.add(film.getId());
            film.setGenres(new LinkedHashSet<>());
        }
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        SqlRowSet rs = jdbcTemplate.queryForRowSet(String.format(GENRE_MAPPER, inSql), ids.toArray());
        while(rs.next()) {
            Long filmId = rs.getLong("FILM_ID");
            Genre genre = Genre.builder()
                    .id(rs.getInt("GENRE_ID"))
                    .name(rs.getString("NAME"))
                    .build();
            LinkedHashSet<Genre> genres = filmIds.get(filmId).getGenres();
            genres.add(genre);
        }
    }
}
