package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.GenreDbStorage;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreStorageDao implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final static String GET_GENRES = "SELECT GENRE_ID FROM GENRE";
    private final static String GET_GENRE_BY_ID = "SELECT * FROM GENRE WHERE GENRE_ID = ?";

    @Override
    public List<Genre> getAll() {
        List<Genre> result = new ArrayList<>();
        List<Integer> genreIds = jdbcTemplate.queryForList(GET_GENRES, Integer.class);
        for (Integer genreId : genreIds) {
            result.add(filmMapper.genreMapper(genreId));
        }
        return result;    }

    @Override
    public Optional<Genre> getById(int id) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(GET_GENRE_BY_ID, id);
        if (genreRow.next()) {
            Genre genre = Genre.builder()
                    .id(genreRow.getInt("GENRE_ID"))
                    .name(genreRow.getString("NAME"))
                    .build();
            return Optional.of(genre);
        }
        return Optional.empty();
    }
}
