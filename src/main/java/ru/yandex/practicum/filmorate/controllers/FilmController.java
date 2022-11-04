package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import static ru.yandex.practicum.filmorate.model.Film.FILM_ENDPOINT;

@RestController
@RequestMapping(FILM_ENDPOINT)
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public void createFilm(@RequestBody Film film) {
        Validator.filmValidator(film);
        log.debug("Получен запрос POST {}", FILM_ENDPOINT);
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " уже существует.");
        }
        films.put(film.getId(), film);
    }

    @PutMapping
    public void updateFilm(@RequestBody Film film) {
        Validator.filmValidator(film);
        log.debug("Получен запрос PUT {}", FILM_ENDPOINT);
        films.put(film.getId(), film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }
}
