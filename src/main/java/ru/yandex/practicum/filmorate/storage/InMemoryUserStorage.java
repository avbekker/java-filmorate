package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    @Override
    public void createUser(User user) {
        Validator.userValidator(user);
        user.setId(++id);
        users.put(user.getId(), user);
    }
    @Override
    public void deleteUser(User user) {
        users.remove(user.getId(), user);
    }
    @Override
    public void updateUser(User user) {
        Validator.userValidator(user);
        users.put(user.getId(), user);
    }
    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
