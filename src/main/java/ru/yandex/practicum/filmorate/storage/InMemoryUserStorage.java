package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validator;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    @Override
    public User createUser(User user) {
        Validator.userValidator(user);
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }
    @Override
    public void deleteUser(User user) {
        users.remove(user.getId(), user);
    }
    @Override
    public User updateUser(User user) {
        Validator.userValidator(user);
        users.put(user.getId(), user);
        return user;
    }
    @Override
    public Map<Integer, User> getUsers() {
        return new HashMap<>(users);
    }
}
