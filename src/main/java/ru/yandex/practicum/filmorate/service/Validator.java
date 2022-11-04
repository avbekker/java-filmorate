package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Validator {
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

//Проверка характеристик пользователя
    public static void userValidator(User user) {
        if (!isValidEmail(user)) {
            throw new ValidationException("Е-мэйл пользователя некорректен.");
        }
        if (!isValidLogin(user)) {
            throw new ValidationException("Логин пользователя некорректен.");
        }
        if (!isValidBirthday(user)) {
            throw new ValidationException("Дата рождения некорректна.");
        }
        if (!isValidName(user) && isValidLogin(user)) {
            user.setName(user.getLogin());
        }
        if (isValidLogin(user) && isValidEmail(user) && isValidBirthday(user)) {
            System.out.print("Валидация пользователя прошла успешно.");
        }
    }
    private static boolean isValidEmail(User user) {
        return user.getEmail() != null && !user.getEmail().isBlank() && user.getEmail().contains("@");
    }
    private static boolean isValidLogin(User user) {
        return user.getLogin() != null && !user.getLogin().isBlank() && !user.getLogin().contains(" ");
    }
    private static boolean isValidName(User user) {
        return user.getName() != null && !user.getName().isBlank();
    }
    private static boolean isValidBirthday(User user) {
        return user.getBirthday().isBefore(LocalDate.now());
    }

//Проверка характеристик фильма
    public static void filmValidator(Film film) {
        if (isEmptyFilmName(film)) {
            throw new ValidationException("Название фильма некореектно.");
        }
        if (!isValidFilmDuration(film)) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
        if (!isValidLengthFilmDescription(film)) {
            throw new ValidationException("Описание фильма не может превышать 200 символов.");
        }
        if (!isValidReleaseDate(film)) {
            throw new ValidationException("Дата релиза должна быть позже " + EARLIEST_RELEASE_DATE);
        }
        if (!isEmptyFilmName(film) && isValidReleaseDate(film) &&
                isValidFilmDuration(film) && isValidLengthFilmDescription(film)) {
            System.out.print("Валидация фильма прошла успешно.");
        }
    }
    private static boolean isEmptyFilmName(Film film) {
        return film.getName() == null || film.getName().isEmpty();
    }
    private static boolean isValidLengthFilmDescription(Film film) {
        return film.getDescription().getBytes(StandardCharsets.UTF_8).length <= MAX_LENGTH_DESCRIPTION;
    }
    private static boolean isValidReleaseDate(Film film) {
        return film.getReleaseDate().isAfter(EARLIEST_RELEASE_DATE);
    }
    private static boolean isValidFilmDuration(Film film) {
        return film.getDuration() >= 0;
    }
}
