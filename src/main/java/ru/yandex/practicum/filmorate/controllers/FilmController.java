package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;
    private final InMemoryFilmStorage storage;
    @Autowired
    public FilmController(FilmService service, InMemoryFilmStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма {}", film.getName());
        storage.createFilm(film);
        return film;
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        existingValidation(film.getId());
        log.info("Обновление фильма {}", film.getName());
        storage.updateFilm(film);
        return film;
    }
    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(storage.getFilms().values());
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        service.setLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        existingValidation(filmId);
        if (!storage.getFilms().get(filmId).getLikes().contains(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не ставил лайк фильму "
                    + storage.getFilms().get(filmId).getName());
        }
        log.info("Пользователь с ID {} убрал лайк от фильма с ID {}", userId, filmId);
        service.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        log.info("Получение списка фильмов с наибольшим количеством лайков");
        return service.getTop(count);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        existingValidation(filmId);
        log.info("Получение фильма с ID {}", filmId);
        return storage.getFilms().get(filmId);
    }

    private void existingValidation(int filmId) {
        if (!storage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильма с ID " + filmId + " не существует.");
        }
    }
}
