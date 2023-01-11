package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.*;

public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Integer, Genre> genres = new HashMap<>();
    private final Map<Integer, Mpa> MPAs = new HashMap<>();
    @Override
    public void create(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return Optional.of(genres.get(id));
    }

    @Override
    public List<Mpa> getMPAs() {
        return new ArrayList<>(MPAs.values());
    }

    @Override
    public Optional<Mpa> getMPAById(int id) {
        return Optional.of(MPAs.get(id));
    }
}
