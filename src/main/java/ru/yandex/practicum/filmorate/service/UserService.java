package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
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
    public void deleteFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public Set<User> getMutualFriends(User user, User friend) {
        List<Integer> mutualIds = user.getFriends().stream()
                .filter(friend.getFriends()::contains).collect(Collectors.toList());
        return storage.getUsers().stream().filter(u -> mutualIds.contains(u.getId())).collect(Collectors.toSet());
    }
}
