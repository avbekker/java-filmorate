package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@Validated
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenresController {
    private final FilmService service;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получение списка всех жанров.");
        return service.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Получение жанра с идентификатором {}", id);
        return service.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с ID " + id + " не существует."));
    }
}
