package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.Validator;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        Validator.userValidator(user);
        log.info("Новый пользователь {} создан", user.getLogin());
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (service.getUsers().stream().noneMatch(f -> f.getId() == user.getId())) {
                throw new NotFoundException("Пользователя с таким ID не существует.");
        }
        Validator.userValidator(user);
        log.info("Обновление пользователя {}", user.getLogin());
        service.update(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(service.getUsers());
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        User user = service.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        log.info("Получение пользователя с ID = {}", userId);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void makeFriends(@PathVariable long userId, @PathVariable long friendId) {
        User user = service.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        User friend = service.getById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        service.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        User user = service.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        User friend = service.getById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        service.deleteFriend(user, friend);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable long userId) {
        log.info("Получение списка друзей пользователя с ID = {}", userId);
        User user = service.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        Set<Long> friendList = user.getFriends();
        return service.getUsers().stream()
                .filter(f -> friendList.contains(f.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long userId, @PathVariable long otherId) {
        log.info("Получение списка общих друзей пользователей c ID {} и {}", userId, otherId);
        User user = service.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        User otherUser = service.getById(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        Set<User> mutualFriends = service.getMutualFriends(user, otherUser);
        return new ArrayList<>(mutualFriends);
    }
}
