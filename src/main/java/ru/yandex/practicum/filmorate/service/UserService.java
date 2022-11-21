package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    final InMemoryUserStorage storage;
    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }
    public void addFriend(int userId, int friendId) {
        User user = storage.getUsers().get(userId);
        User friend = storage.getUsers().get(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }
    public void deleteFriend(int userId, int friendId) {
        User user = storage.getUsers().get(userId);
        User friend = storage.getUsers().get(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public Set<User> getMutualFriends(User user, User friend) {
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(storage.getUsers()::get)
                .collect(Collectors.toSet());
    }
}
