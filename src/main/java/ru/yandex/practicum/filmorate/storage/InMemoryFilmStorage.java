package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validator;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public Film createFilm(Film film) {
        Validator.filmValidator(film);
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }
    @Override
    public void deleteFilm(Film film) {
        films.remove(film.getId(), film);
    }
    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с id " + film.getId() + " не существует.");
        }
        Validator.filmValidator(film);
        films.put(film.getId(), film);
        return film;
    }
    @Override
    public Map<Integer, Film> getFilms() {
        return new HashMap<>(films);
    }
}
