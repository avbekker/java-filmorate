package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    public static final String FILM_ENDPOINT = "/films";
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
}
