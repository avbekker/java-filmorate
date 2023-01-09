package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.MpaDbStorage;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaStorageDao implements MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    //исправить
    private final FilmMapper filmMapper;
    private final static String GET_ALL_MPA = "SELECT MPA_ID FROM MPA";
    private final static String GET_MPA_BY_ID = "SELECT * FROM MPA WHERE MPA_ID = ?";

    @Override
    public List<Mpa> getAll() {
        List<Mpa> result = new ArrayList<>();
        List<Integer> mpaIds = jdbcTemplate.queryForList(GET_ALL_MPA, Integer.class);
        for (Integer mpaId : mpaIds) {
            result.add(filmMapper.mpaMapper(mpaId));
        }
        return result;
    }

    @Override
    public Optional<Mpa> getById(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(GET_MPA_BY_ID, id);
        if (mpaRow.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRow.getInt("MPA_ID"))
                    .name(mpaRow.getString("NAME"))
                    .build();
            return Optional.of(mpa);
        }
        return Optional.empty();
    }
}
