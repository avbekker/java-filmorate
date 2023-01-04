package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDbStorage {
    User create(User user);
    void delete(User user);
    User update(User user);
    List<User> getUsers();
    Optional<User> getById(long id);
    void makeFriend(User user, User friend);
    void deleteFriend(User user, User friend);
}
