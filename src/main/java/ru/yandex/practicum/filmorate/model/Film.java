package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    public static final String FILM_ENDPOINT = "/films";
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

}
