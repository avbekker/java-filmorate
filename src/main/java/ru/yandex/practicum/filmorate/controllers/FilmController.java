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
        return service.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        Film validatedFilm = service.getById(film.getId());
        Validator.filmValidator(film);
        log.info("Обновление фильма {}", validatedFilm.getName());
        return service.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(service.getFilms());
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable long filmId, @PathVariable long userId) {
        Film film = service.getById(filmId);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        service.setLike(userId, film);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        Film validatedFilm = service.getById(filmId);
        if (userId <= 0) {
            throw new NotFoundException("Not found.");
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
        Film validatedFilm = service.getById(filmId);
        log.info("Получение фильма с ID {}", filmId);
        return validatedFilm;
    }
}
