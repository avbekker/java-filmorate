package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
@Data
@Builder
public class User {
    public static final String USER_ENDPOINT = "/users";
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
