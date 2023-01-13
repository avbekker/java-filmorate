package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDbStorage {
    User create(User user);
    void delete(User user);
    User update(User user);
    List<User> getAll();
    Optional<User> getById(long id);
}
