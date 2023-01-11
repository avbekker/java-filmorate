package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface FriendsDbStorage {
    void makeFriend(User user, User friend);
    void deleteFriend(User user, User friend);
    List<User> getFriends(long userId);
    List<User> getMutualFriends(long userId, long friendId);
}
