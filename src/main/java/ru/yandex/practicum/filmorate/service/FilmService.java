package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    final InMemoryFilmStorage storage;
    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }
    public void setLike(Integer userId, Integer filmId) {
        storage.getFilms().get(filmId).getLikes().add(userId);
    }
    public void deleteLike(Integer userId, Integer filmId) {
        storage.getFilms().get(filmId).getLikes().remove(userId);
    }
    public List<Film> getTop(Integer count) {
        if (count == null) {
            count = 10;
        }
        return storage.getFilms().values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
