package ru.yandex.practicum.filmorate.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final static String GENRE_MAPPER = "SELECT G.GENRE_ID FROM GENRE G INNER JOIN FILM_GENRE FG ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?";
    private final static String LIKE_MAPPER = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
    private final static String MPA_MAPPER = "SELECT * FROM MPA WHERE MPA_ID = ?";
    private final static String GENRE_BY_ID = "SELECT * FROM GENRE WHERE GENRE_ID = ?";

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
                .mpa(mpaMapper(rs.getInt("MPA_ID")))
                .genres(genreMapping(rs.getLong("FILM_ID")))
                .likes(likeMapper(rs.getLong("FILM_ID")))
                .build();
    }

    public List<Genre> genreMapping(long filmId){
        List<Genre> genreList = new ArrayList<>();
        List<Integer> genreIds = jdbcTemplate.queryForList(GENRE_MAPPER, Integer.class, filmId);
        for (Integer genreId : genreIds) {
            genreList.add(
                    genreMapper(genreId)
            );
        }
        return genreList;
    }

    public Genre genreMapper(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GENRE_BY_ID, id);
        if (rs.next()) {
            return Genre.builder()
                    .id(rs.getInt("GENRE_ID"))
                    .name(rs.getString("NAME"))
                    .build();
        }
        return null;
    }

    private Set<Long> likeMapper(long filmId){
        List<Long> likes = jdbcTemplate.queryForList(LIKE_MAPPER, Long.class, filmId);
        return new HashSet<>(likes);
    }

    public MPA mpaMapper(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(MPA_MAPPER, id);
        if (mpaRow.next()) {
            return MPA.builder()
                    .id(mpaRow.getInt("MPA_ID"))
                    .name(mpaRow.getString("NAME"))
                    .build();
        }
        return null;
    }
}
