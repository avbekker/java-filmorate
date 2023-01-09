package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage storage;

    public void setLike(Long userId, Film film) {
        storage.setLike(userId, film);
    }

    public void deleteLike(Long userId, Film film) {
        storage.deleteLike(userId, film);
    }

    public List<Film> getTop(Long count) {
        return storage.getFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        return storage.create(film);
    }

    public void delete(Film film) {
        storage.delete(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film getById(long id) {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с таким ID не найден."));
    }

    public List<Genre> getGenres() {
        return storage.getGenres();
    }

    public Genre getGenreById(int id) {
        return storage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с ID " + id + " не существует."));
    }

}
