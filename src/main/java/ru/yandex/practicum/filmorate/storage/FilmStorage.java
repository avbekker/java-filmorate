package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void create(Film film);
    void delete(Film film);
    void update(Film film);
    List<Film> getFilms();
    Optional<Film> getById(long id);
}
