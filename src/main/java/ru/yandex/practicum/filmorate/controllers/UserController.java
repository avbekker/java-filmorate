package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validator;
import javax.validation.Valid;
import java.util.*;
import static ru.yandex.practicum.filmorate.model.User.USER_ENDPOINT;

@RestController
@RequestMapping(USER_ENDPOINT)
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        Validator.userValidator(user);
        log.debug("Получен запрос POST {}", USER_ENDPOINT);
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с id " + user.getId() + " не существует.");
        }
        Validator.userValidator(user);
        log.debug("Получен запрос PUT {}", USER_ENDPOINT);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

}
