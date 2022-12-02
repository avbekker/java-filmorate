package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.Validator;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@RestController
@Validated
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Validator.filmValidator(film);
        log.info("Добавление нового фильма {}", film.getName());
        service.create(film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        Film validatedFilm = getById(film.getId());
        Validator.filmValidator(film);
        log.info("Обновление фильма {}", validatedFilm.getName());
        service.update(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(service.getFilms());
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable long filmId, @PathVariable long userId) {
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        Film film = getById(filmId);
        service.setLike(userId, film);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        Film validatedFilm = getById(filmId);
        if (!validatedFilm.getLikes().contains(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не ставил лайк фильму "
                    + validatedFilm.getName());
        }
        log.info("Пользователь с ID {} убрал лайк от фильма с ID {}", userId, validatedFilm.getId());
        service.deleteLike(userId, validatedFilm);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(defaultValue = "10", required = false)  Long count) {
        log.info("Получение списка фильмов с наибольшим количеством лайков");
        return service.getTop(count);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        Film validatedFilm = getById(filmId);
        log.info("Получение фильма с ID {}", filmId);
        return validatedFilm;
    }

    private Film getById(long filmId) {
        return service.getById(filmId).orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
    }
}
