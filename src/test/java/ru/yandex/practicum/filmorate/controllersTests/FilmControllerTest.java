package ru.yandex.practicum.filmorate.controllersTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final FilmStorageDao dbStorage;

    @Test
    public void createFilmTest(){
        Film film = Film.builder()
                .id(1)
                .name("Kill Bill")
                .description("Some lady want to kill her ex boyfriend with big knife.")
                .duration(120)
                .releaseDate(LocalDate.of(2003, 9, 23))
                .mpa(Mpa.builder()
                        .id(1)
                        .build())
                .build();
        dbStorage.create(film);
        assertEquals(3, film.getId());
    }

    @Test
    public void updateFilmTest(){

        dbStorage.update(
                Film.builder()
                        .id(1)
                        .name("Kill Bill updated")
                        .description("Some lady want to kill her ex boyfriend with big knife.")
                        .duration(120)
                        .releaseDate(LocalDate.of(2003, 9, 23))
                        .mpa(Mpa.builder()
                                .id(1)
                                .build())
                        .build()
        );
        assertEquals(dbStorage.getById(1).get().getName(), "Kill Bill updated");
    }

    @Test
    public void getAllFilms(){
        dbStorage.create(Film.builder()
                .id(1)
                .name("Kill Bill")
                .description("Some lady want to kill her ex boyfriend with big knife.")
                .duration(120)
                .releaseDate(LocalDate.of(2003, 9, 23))
                .mpa(Mpa.builder()
                        .id(1)
                        .build())
                .build());
        dbStorage.create(Film.builder()
                .id(2)
                .name("Kill Bill 2")
                .description("Lady from previous part want to kill her ex boyfriend with big knife again.")
                .duration(120)
                .releaseDate(LocalDate.of(2004, 6, 17))
                .mpa(Mpa.builder()
                        .id(1)
                        .build())
                .build());
        List<Film> films = dbStorage.getFilms();
        assertEquals(2, films.size());
    }

    @Test
    public void getFilmById(){

        assertThat(dbStorage.getById(1))
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
    }
}
