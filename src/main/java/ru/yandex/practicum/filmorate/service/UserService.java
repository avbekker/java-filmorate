package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.FriendsDbStorage;
import ru.yandex.practicum.filmorate.dao.interf.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage storage;
    private final FriendsDbStorage friendsDbStorage;

    public void addFriend(User user, User friend) {
        friendsDbStorage.makeFriend(user, friend);
    }

    public void deleteFriend(User user, User friend) {
        friendsDbStorage.deleteFriend(user, friend);
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        return friendsDbStorage.getMutualFriends(userId, friendId);
    }

    public User create(User user) {
        return storage.create(user);
    }

    public void delete(User user) {
        storage.delete(user);
    }

    public void update(User user) {
        storage.update(user);
    }

    public List<User> getUsers() {
        return storage.getAll();
    }

    public User getById(long id) {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким ID не существует."));
    }

    public List<User> getFriends(long userId) {
        return friendsDbStorage.getFriends(userId);
    }
}
