package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@Validated
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final FilmService service;

    @GetMapping
    public List<MPA> getMPAs() {
        log.info("Получение списка всех рейтингов");
        return service.getMPAs();
    }

    @GetMapping("/{id}")
    public MPA getMPAById(@PathVariable int id) {
        log.info("Получение рейтинга с идентификатором {}", id);
        return service.getMPAById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID " + id + " не существует."));
    }
}
