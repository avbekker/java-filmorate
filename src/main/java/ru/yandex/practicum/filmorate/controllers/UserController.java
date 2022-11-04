package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import static ru.yandex.practicum.filmorate.model.User.USER_ENDPOINT;

@RestController
@RequestMapping(USER_ENDPOINT)
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public void createUser(@RequestBody User user) {
        Validator.userValidator(user);
        log.debug("Получен запрос POST {}", USER_ENDPOINT);
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " уже существует.");
        }
        users.put(user.getId(), user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        Validator.userValidator(user);
        log.debug("Получен запрос PUT {}", USER_ENDPOINT);
        users.put(user.getId(), user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

}
