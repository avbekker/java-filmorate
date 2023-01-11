package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenresController {
    private final GenreService service;

    @GetMapping
    public List<Genre> getAll() {
        log.info("Получение списка всех жанров.");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        log.info("Получение жанра с идентификатором {}", id);
        return service.getGenreById(id);
    }
}
