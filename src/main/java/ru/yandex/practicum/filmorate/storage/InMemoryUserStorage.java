package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void create(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
