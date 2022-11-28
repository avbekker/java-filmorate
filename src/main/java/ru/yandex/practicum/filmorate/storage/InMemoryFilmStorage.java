package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public void createFilm(Film film) {
        Validator.filmValidator(film);
        film.setId(++id);
        films.put(film.getId(), film);
    }
    @Override
    public void deleteFilm(Film film) {
        films.remove(film.getId(), film);
    }
    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с id " + film.getId() + " не существует.");
        }
        Validator.filmValidator(film);
        films.put(film.getId(), film);
    }
    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
