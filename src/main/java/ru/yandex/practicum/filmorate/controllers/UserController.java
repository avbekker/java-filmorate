package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService service;
    InMemoryUserStorage storage;
    @Autowired
    public UserController(UserService service, InMemoryUserStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Новый пользователь {} создан", user.getLogin());
        storage.createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        existingValidation(user.getId());
        log.info("Обновление пользователя {}", user.getLogin());
        storage.updateUser(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(storage.getUsers().values());
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        existingValidation(userId);
        log.info("Получение пользователя с ID = {}", userId);
        return storage.getUsers().get(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void makeFriends(@PathVariable int userId, @PathVariable int friendId) {
        if (storage.getUsers().containsKey(userId) && storage.getUsers().containsKey(friendId)) {
            service.addFriend(userId, friendId);
        } else {
            throw new NotFoundException("Пользователя с таким ID не существует.");
        }
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        service.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable int userId) {
        log.info("Получение списка друзей пользователя с ID = {}", userId);
        Set<Integer> friendList = storage.getUsers().get(userId).getFriends();
        return storage.getUsers().values().stream()
                .filter(f -> friendList.contains(f.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("Получение списка общих друзей пользователей c ID {} и {}", userId, otherId);
        Set<User> mutualFriends = service.getMutualFriends(storage.getUsers().get(userId), storage.getUsers().get(otherId));
        return new ArrayList<>(mutualFriends);
    }

    private void existingValidation(int userId) {
        if (!storage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователся с ID " + userId + " не существует.");
        }
    }
}
