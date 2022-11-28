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
    public void createFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма {}", film.getName());
        service.getStorage().createFilm(film);
    }
    @PutMapping
    public void updateFilm(@Valid @RequestBody Film film) {
        Film validatedFilm = Optional.of(service.getStorage().getFilms().get(film.getId()))
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + film.getId() + " не существует."));
        log.info("Обновление фильма {}", validatedFilm.getName());
        service.getStorage().updateFilm(validatedFilm);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(service.getStorage().getFilms());
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        service.setLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        Film validatedFilm = Optional.of(service.getStorage().getFilms().get(filmId))
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
        if (!validatedFilm.getLikes().contains(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не ставил лайк фильму "
                    + validatedFilm.getName());
        }
        log.info("Пользователь с ID {} убрал лайк от фильма с ID {}", userId, validatedFilm.getId());
        service.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(defaultValue = "10", required = false)  Integer count) {
        log.info("Получение списка фильмов с наибольшим количеством лайков");
        return service.getTop(count);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        Film validatedFilm = Optional.of(service.getStorage().getFilms().get(filmId))
                .orElseThrow(() -> new NotFoundException("Фильма с ID " + filmId + " не существует."));
        log.info("Получение фильма с ID {}", filmId);
        return validatedFilm;
    }
}
