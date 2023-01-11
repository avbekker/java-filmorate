package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaStorageDao implements MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    private final static String GET_ALL_MPA = "SELECT * FROM MPA";
    private final static String GET_MPA_BY_ID = "SELECT * FROM MPA WHERE MPA_ID = ?";

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(GET_ALL_MPA, mpaMapper);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject(GET_MPA_BY_ID, mpaMapper, id);
            if (mpa == null) {
                throw new NotFoundException("Not found");
            }
            return Optional.of(mpa);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found", e);
        }
    }
}
