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
    public User createUser(@Valid @RequestBody User user) {
        log.info("Новый пользователь {} создан", user.getLogin());
        service.getStorage().createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (service.getStorage().getUsers().stream().noneMatch(f -> f.getId() == user.getId())) {
                throw new NotFoundException("Пользователя с таким ID не существует.");
        }
        log.info("Обновление пользователя {}", user.getLogin());
        service.getStorage().updateUser(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(service.getStorage().getUsers());
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        User user = getUserById(userId);
        log.info("Получение пользователя с ID = {}", userId);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void makeFriends(@PathVariable int userId, @PathVariable int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        service.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        service.deleteFriend(user, friend);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable int userId) {
        log.info("Получение списка друзей пользователя с ID = {}", userId);
        User user = getUserById(userId);
        Set<Integer> friendList = user.getFriends();
        return service.getStorage().getUsers().stream()
                .filter(f -> friendList.contains(f.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("Получение списка общих друзей пользователей c ID {} и {}", userId, otherId);
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);
        Set<User> mutualFriends = service.getMutualFriends(user, otherUser);
        return new ArrayList<>(mutualFriends);
    }

    private User getUserById(int id) {
        return service.getStorage().getUsers().stream().filter(u -> u.getId() == id).findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
    }
}
