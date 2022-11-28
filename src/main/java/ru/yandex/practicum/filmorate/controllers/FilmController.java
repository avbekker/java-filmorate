package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@RestController
@Validated
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;
    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма {}", film.getName());
        service.getStorage().createFilm(film);
        return film;
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film validatedFilm = getValidatedFilm(film.getId());
        log.info("Обновление фильма {}", validatedFilm.getName());
        service.getStorage().updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(service.getStorage().getFilms());
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        Film film = getValidatedFilm(filmId);
        service.setLike(userId, film);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        Film validatedFilm = getValidatedFilm(filmId);
        if (!validatedFilm.getLikes().contains(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не ставил лайк фильму "
                    + validatedFilm.getName());
        }
        log.info("Пользователь с ID {} убрал лайк от фильма с ID {}", userId, validatedFilm.getId());
        service.deleteLike(userId, validatedFilm);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(defaultValue = "10", required = false)  Integer count) {
        log.info("Получение списка фильмов с наибольшим количеством лайков");
        return service.getTop(count);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        Film validatedFilm = getValidatedFilm(filmId);
        log.info("Получение фильма с ID {}", filmId);
        return validatedFilm;
    }

    private Film getValidatedFilm(int filmId) {
        return service.getStorage().getFilms().stream().filter(f -> f.getId() == filmId).findFirst()
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
    }
}
