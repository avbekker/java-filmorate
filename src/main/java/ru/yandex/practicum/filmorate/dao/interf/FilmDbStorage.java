package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmDbStorage {
    Film create(Film film);
    void delete(Film film);
    Film update(Film film);
    List<Film> getFilms();
    Optional<Film> getById(long id);
}
