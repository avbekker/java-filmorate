package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage storage;

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getGenreById(int id) {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с ID " + id + " не существует."));
    }
}
