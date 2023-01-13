package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.interf.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.interf.LikesDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final LikesDbStorage likeStorage;
    private final GenreDbStorage genreStorage;

    public void setLike(Long userId, Film film) {
        likeStorage.setLike(userId, film);
    }

    public void deleteLike(Long userId, Film film) {
        likeStorage.deleteLike(userId, film);
    }

    public List<Film> getTop(Long count) {
        List<Film> films = likeStorage.getTop(count);
        genreStorage.addGenresToFilms(films);
        return films;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public void delete(Film film) {
        filmStorage.delete(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        genreStorage.addGenresToFilms(films);
        return films;
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с таким ID не найден."));
        genreStorage.addGenresToFilms(List.of(film));
        return film;
    }
}
