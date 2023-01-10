package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.LikesDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikesStorageDao implements LikesDbStorage {
    private final JdbcTemplate jdbcTemplate;

    private final static String SET_LIKE = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
    private final static String DELETE_LIKE = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
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
}
