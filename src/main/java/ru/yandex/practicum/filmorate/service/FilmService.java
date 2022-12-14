package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public void setLike(Long userId, Film film) {
        film.getLikes().add(userId);
    }

    public void deleteLike(Long userId, Film film) {
        film.getLikes().remove(userId);
    }

    public List<Film> getTop(Long count) {
        return storage.getFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void create(Film film) {
        storage.create(film);
    }

    public void delete(Film film) {
        storage.delete(film);
    }

    public void update(Film film) {
        storage.update(film);
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Optional<Film> getById(long id) {
        return storage.getById(id);
    }
}
