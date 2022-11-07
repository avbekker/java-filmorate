package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
@Data
@Builder
public class User {
    public static final String USER_ENDPOINT = "/users";
    private int id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
}
