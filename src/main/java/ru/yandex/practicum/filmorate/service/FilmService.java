package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.interf.LikesDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage storage;
    private final LikesDbStorage likesDbStorage;

    public void setLike(Long userId, Film film) {
        likesDbStorage.setLike(userId, film);
    }

    public void deleteLike(Long userId, Film film) {
        likesDbStorage.deleteLike(userId, film);
    }

    public List<Film> getTop(Long count) {
        return likesDbStorage.getTop(count);
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
}
