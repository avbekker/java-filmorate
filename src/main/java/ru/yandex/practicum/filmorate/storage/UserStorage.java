package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    void createUser(User user);
    void deleteUser(User user);
    void updateUser(User user);
    List<User> getUsers();
}
