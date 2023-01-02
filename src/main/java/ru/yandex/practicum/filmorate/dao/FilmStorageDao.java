package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmStorageDao implements FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void create(Film film) {
        String query = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                " VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa());
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.execute("DELETE FROM FILMS WHERE FILM_ID = " + film.getId() + "");
    }

    @Override
    public void update(Film film) {
        String query = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?" +
                " WHERE FILM_ID = ?";
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT * FROM FILMS" +
                " LEFT JOIN MPA M on FILMS.MPA_ID = M.MPA_ID" +
                " LEFT JOIN FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID";
        return jdbcTemplate.queryForList(query, Film.class);
    }

    @Override
    public Optional<Film> getById(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id);
        if (filmRows.next()) {
            log.info("Найден пользователь: {} {}", filmRows.getString("FILM_ID"),
                    filmRows.getString("NAME"));
            SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ?",
                    filmRows.getInt("MPA_ID"));
            MPA mpa = MPA.builder()
                    .id(mpaRow.getInt("MPA_ID"))
                    .name(mpaRow.getString("NAME"))
                        .build();
            Film film = Film.builder()
                    .id(filmRows.getLong("FILM_ID"))
                    .name(filmRows.getString("FILM_NAME"))
                    .description(filmRows.getString("DESCRIPTION"))
                    .duration(filmRows.getInt("DURATION"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate())
                    .mpa(mpa)
                        .build();
            return Optional.of(film);
        }
        log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    public List<Genre> getGenres() {
        String query = "SELECT * FROM GENRE";
        return jdbcTemplate.queryForList(query, Genre.class);
    }

    public Optional<Genre> getGenreById(int id) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?", id);
        if (genreRow.next()) {
            Genre genre = Genre.builder()
                    .id(genreRow.getInt("GENRE_ID"))
                    .name(genreRow.getString("NAME"))
                    .build();
            log.info("Найден жанр: {}.", genre.getName());
            return Optional.of(genre);
        }
        log.info("Жанр с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    public List<MPA> getMPAs() {
        String query = "SELECT * FROM MPA";
        return jdbcTemplate.queryForList(query, MPA.class);
    }

    public Optional<MPA> getMPAById(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ?", id);
        if (mpaRow.next()) {
            MPA mpa = MPA.builder()
                    .id(mpaRow.getInt("MPA_ID"))
                    .name(mpaRow.getString("NAME"))
                    .build();
            log.info("Найден рейтинг: {}.", mpa.getName());
            return Optional.of(mpa);
        }
        log.info("Рейтинг с идентификатором {} не найден.", id);
        return Optional.empty();
    }
}
