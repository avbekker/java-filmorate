package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .duration(rs.getInt("DURATION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .mpa(Mpa.builder()
                        .id(rs.getInt("MPA_ID"))
                        .name(rs.getString("MPA_NAME"))
                        .build())
                .build();
    }
}
