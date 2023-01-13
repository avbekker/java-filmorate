package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.LikesDbStorage;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikesStorageDao implements LikesDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    private final static String SET_LIKE = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
    private final static String DELETE_LIKE = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";

    private static final String GET_TOP = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA_ID, M.NAME AS MPA_NAME " +
            "FROM FILM_LIKES AS FL RIGHT JOIN FILMS AS F ON FL.FILM_ID = F.FILM_ID " +
            "LEFT JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
            "GROUP BY F.FILM_ID ORDER BY COUNT(FL.FILM_ID) DESC LIMIT ?";

    @Override
    public void setLike(Long userId, Film film) {
        jdbcTemplate.update(SET_LIKE, film.getId(), userId);
    }

    @Override
    public void deleteLike(Long userId, Film film) {
        jdbcTemplate.update(DELETE_LIKE, film.getId(), userId);
    }

    @Override
    public List<Film> getTop(Long count) {
        return jdbcTemplate.query(GET_TOP, filmMapper, count);
    }
}
