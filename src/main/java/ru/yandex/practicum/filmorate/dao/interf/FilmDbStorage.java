package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;
import java.util.Optional;

public interface FilmDbStorage {
    Film create(Film film);
    void delete(Film film);
    Film update(Film film);
    List<Film> getFilms();
    Optional<Film> getById(long id);
    List<Genre> getGenres();
    Optional<Genre> getGenreById(int id);
    List<Mpa> getMpa();
    Optional<Mpa> getMpaById(int id);
    void setLike(Long userId, Film film);
    void deleteLike(Long userId, Film film);
}
