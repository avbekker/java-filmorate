package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDbStorage {
    List<Mpa> getAll();
    Optional<Mpa> getById(int id);
}
