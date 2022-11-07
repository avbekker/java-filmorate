package ru.yandex.practicum.filmorate.controllersTests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController controller = new FilmController();
    Film goodFilm = Film.builder().id(1).name("Kill Bill")
            .description("Some lady want to kill her ex boyfriend with big knife.")
            .duration(120).releaseDate(LocalDate.of(2003, 9, 23)).build();
    Film goodFilm2 = Film.builder().id(2).name("Kill Bill 2")
            .description("Lady from previous part want to kill her ex boyfriend with big knife again.")
            .duration(120).releaseDate(LocalDate.of(2004, 6, 17)).build();

    @Test
    public void createNewFilmTest(){
        controller.createFilm(goodFilm);
        assertEquals(1, controller.getFilms().size());
        controller.createFilm(goodFilm2);
        assertEquals(2, controller.getFilms().size());
    }
    @Test
    public void shouldFailCreationWithEmptyName(){
        Film filmWithEmptyName = Film.builder().id(1).name("")
                .description("Some lady want to kill her ex boyfriend with big knife.")
                .duration(120).releaseDate(LocalDate.of(2003, 9, 23)).build();
        assertThrows(ValidationException.class, () -> controller.createFilm(filmWithEmptyName));
    }
    @Test
    public void shouldFailCreationWithNullName(){
        Film filmWithEmptyName = Film.builder().id(1)
                .name(null)
                .description("Some lady want to kill her ex boyfriend with big knife.").duration(120)
                .releaseDate(LocalDate.of(2003, 9, 23)).build();
        assertThrows(ValidationException.class, () -> controller.createFilm(filmWithEmptyName));
    }
    @Test
    public void shouldFailCreationWithDescriptionMoreThan200Characters(){
        Film film = Film.builder().id(1).name("Kill Bill")
                .description("Some lady want to kill her ex boyfriend with big knife. This test for description more " +
                        "than 200 characters, but I do not know what can I write in this section to rise description " +
                        "up to 200 characters. ")
                .duration(120).releaseDate(LocalDate.of(2003, 9, 23)).build();
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
    @Test
    public void shouldCreateWithDescriptionEquals200Characters(){
        Film film = Film.builder().id(1).name("Kill Bill")
                .description("Some lady want to kill her ex boyfriend with big knife. This test for description more " +
                        "than 200 characters, but I do not know what can I write in this section to rise description " +
                        "up to 200 characters")
                .duration(120).releaseDate(LocalDate.of(2003, 9, 23)).build();
        controller.createFilm(film);
        assertEquals(1, controller.getFilms().size());
    }
    @Test
    public void shouldFailCreationWithEarlyReleaseDate(){
        Film film = Film.builder().id(1)
                .name("Kill Bill").description("Some lady want to kill her ex boyfriend with big knife.").duration(120)
                .releaseDate(LocalDate.of(1880, 9, 23))
                .build();
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
    @Test
    public void shouldFailCreationWithNegativeDuration(){
        Film film = Film.builder().id(1).name("Kill Bill").description("Some lady want to kill her ex boyfriend with big knife.")
                .duration(-1)
                .releaseDate(LocalDate.of(2003, 9, 23)).build();
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
    @Test
    public void shouldCreateWith0Duration(){
        Film film = Film.builder().id(1).name("Kill Bill").description("Some lady want to kill her ex boyfriend with big knife.")
                .duration(0)
                .releaseDate(LocalDate.of(2003, 9, 23)).build();
        controller.createFilm(film);
        assertEquals(1, controller.getFilms().size());
    }
}
