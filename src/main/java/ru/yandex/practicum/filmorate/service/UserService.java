package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    public UserStorage getStorage() {
        return storage;
    }

    private final UserStorage storage;
    public void addFriend(User user, User friend) {
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
