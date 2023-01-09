package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final FilmService service;

    @GetMapping
    public List<Mpa> get() {
        log.info("Получение списка всех рейтингов");
        return service.getMpas();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable int id) {
        log.info("Получение рейтинга с идентификатором {}", id);
        return service.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID " + id + " не существует."));
    }
}
