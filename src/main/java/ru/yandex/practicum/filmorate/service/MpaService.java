package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage storage;

    public List<Mpa> getAll() {
        return storage.getAll();
    }

    public Mpa getById(int id) {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID " + id + " не существует."));
    }
}
