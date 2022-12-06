package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void create(User user);
    void delete(User user);
    void update(User user);
    List<User> getUsers();
    Optional<User> getById(long id);

}
