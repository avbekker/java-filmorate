package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
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
        Film validatedFilm = service.getById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + film.getId() + " не существует."));
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
        Film film = service.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
        service.setLike(userId, film);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        Film validatedFilm = service.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
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
        Film validatedFilm = service.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
        log.info("Получение фильма с ID {}", filmId);
        return validatedFilm;
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        log.info("Получение списка всех жанров.");
        return service.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Получение жанра с идентификатором {}", id);
        return service.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с ID " + id + " не существует."));
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAs() {
        log.info("Получение списка всех рейтингов");
        return service.getMPAs();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPAById(@PathVariable int id) {
        log.info("Получение рейтинга с идентификатором {}", id);
        return service.getMPAById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID " + id + " не существует."));
    }
}
