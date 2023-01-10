package ru.yandex.practicum.filmorate.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final static String GENRE_MAPPER = "SELECT G.GENRE_ID, G.NAME FROM GENRE G INNER JOIN FILM_GENRE FG ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?";

    @Autowired
    public FilmMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
                .genres(genreMapper(rs.getLong("FILM_ID")))
                .build();
    }

    public List<Genre> genreMapper(long filmId){
        return jdbcTemplate.query(GENRE_MAPPER, new GenreMapper(), filmId);
    }


}
