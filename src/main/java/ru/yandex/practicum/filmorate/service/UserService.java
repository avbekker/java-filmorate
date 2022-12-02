package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
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
        List<Long> mutualIds = user.getFriends().stream()
                .filter(friend.getFriends()::contains).collect(Collectors.toList());
        return storage.getUsers().stream().filter(u -> mutualIds.contains(u.getId())).collect(Collectors.toSet());
    }

    public void create(User user) {
        storage.create(user);
    }

    public void delete(User user) {
        storage.delete(user);
    }

    public void update(User user) {
        storage.update(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public Optional<User> getById(long id) {
        return storage.getById(id);
    }
}
