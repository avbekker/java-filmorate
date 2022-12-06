package ru.yandex.practicum.filmorate.controllersTests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    InMemoryFilmStorage storage = new InMemoryFilmStorage();
    FilmService service = new FilmService(storage);
    FilmController controller = new FilmController(service);
    Film goodFilm = Film.builder().id(1).name("Kill Bill")
            .description("Some lady want to kill her ex boyfriend with big knife.")
            .duration(120).releaseDate(LocalDate.of(2003, 9, 23)).build();
    Film goodFilm2 = Film.builder().id(2).name("Kill Bill 2")
            .description("Lady from previous part want to kill her ex boyfriend with big knife again.")
            .duration(120).releaseDate(LocalDate.of(2004, 6, 17)).build();

    @Test
    public void createNewFilmTest(){
        controller.create(goodFilm);
        assertEquals(1, controller.getFilms().size());
        controller.create(goodFilm2);
        assertEquals(2, controller.getFilms().size());
    }
    @Test
    public void shouldCreateWithDescriptionEquals200Characters(){
        Film film = Film.builder().id(1).name("Kill Bill")
                .description("Some lady want to kill her ex boyfriend with big knife. This test for description more " +
                        "than 200 characters, but I do not know what can I write in this section to rise description " +
                        "up to 200 characters")
                .duration(120).releaseDate(LocalDate.of(2003, 9, 23)).build();
        controller.create(film);
        assertEquals(1, controller.getFilms().size());
    }
    @Test
    public void shouldFailCreationWithEarlyReleaseDate(){
        Film film = Film.builder().id(1)
                .name("Kill Bill").description("Some lady want to kill her ex boyfriend with big knife.").duration(120)
                .releaseDate(LocalDate.of(1880, 9, 23))
                .build();
        assertThrows(ValidationException.class, () -> controller.create(film));
    }
    @Test
    public void shouldCreateWith0Duration(){
        Film film = Film.builder().id(1).name("Kill Bill").description("Some lady want to kill her ex boyfriend with big knife.")
                .duration(0)
                .releaseDate(LocalDate.of(2003, 9, 23)).build();
        controller.create(film);
        assertEquals(1, controller.getFilms().size());
    }
}
