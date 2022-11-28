package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService service;
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public void createUser(@Valid @RequestBody User user) {
        log.info("Новый пользователь {} создан", user.getLogin());
        service.getStorage().createUser(user);
    }

    @PutMapping
    public void updateUser(@Valid @RequestBody User user) {
        User validatedUser = Optional.of(service.getStorage().getUsers().get(user.getId()))
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        log.info("Обновление пользователя {}", user.getLogin());
        service.getStorage().updateUser(validatedUser);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(service.getStorage().getUsers());
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        User user = Optional.of(service.getStorage().getUsers().get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        log.info("Получение пользователя с ID = {}", userId);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void makeFriends(@PathVariable int userId, @PathVariable int friendId) {
        User user = Optional.of(service.getStorage().getUsers().get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
        User friend = Optional.of(service.getStorage().getUsers().get(friendId))
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
            service.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        service.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable int userId) {
        log.info("Получение списка друзей пользователя с ID = {}", userId);
        Set<Integer> friendList = service.getStorage().getUsers().get(userId).getFriends();
        return service.getStorage().getUsers().stream()
                .filter(f -> friendList.contains(f.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("Получение списка общих друзей пользователей c ID {} и {}", userId, otherId);
        Set<User> mutualFriends = service.getMutualFriends(service.getStorage().getUsers().get(userId),
                service.getStorage().getUsers().get(otherId));
        return new ArrayList<>(mutualFriends);
    }
}
