package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

public class Validator {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

//Проверка характеристик пользователя
    public static void userValidator(User user) {
        if (!isValidName(user) && isValidLogin(user)) {
            user.setName(user.getLogin());
        }
        if (isValidLogin(user)) {
            System.out.print("Валидация пользователя прошла успешно.");
        }
    }

    private static boolean isValidLogin(User user) {
        return !user.getLogin().contains(" ");
    }

    private static boolean isValidName(User user) {
        return user.getName() != null && !user.getName().isBlank();
    }

//Проверка характеристик фильма
    public static void filmValidator(Film film) {
        if (!isValidReleaseDate(film)) {
            throw new ValidationException("Дата релиза должна быть позже " + EARLIEST_RELEASE_DATE);
        }
        if (isValidReleaseDate(film)) {
            System.out.print("Валидация фильма прошла успешно.");
        }
    }

    private static boolean isValidReleaseDate(Film film) {
        return film.getReleaseDate().isAfter(EARLIEST_RELEASE_DATE);
    }
}
