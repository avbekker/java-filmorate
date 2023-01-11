package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDbStorage {
    List<Genre> getAll();
    Optional<Genre> getById(int id);
    void addGenresToFilms(List<Film> films);
}
