package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.FriendsDbStorage;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FriendsStorageDao implements FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    private final static String GET_FRIENDS_OF_USER = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID =?)";
    private final static String MAKE_FRIEND = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
    private final static String DELETE_FRIEND = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String GET_MUTUAL_FRIENDS = "SELECT * FROM USERS U, FRIENDS F, FRIENDS O " +
            "WHERE U.USER_ID = F.FRIEND_ID AND U.USER_ID = O.FRIEND_ID AND F.USER_ID = ? AND O.USER_ID = ?";

    @Override
    public void makeFriend(User user, User friend) {
        jdbcTemplate.update(MAKE_FRIEND, user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        jdbcTemplate.update(DELETE_FRIEND, user.getId(), friend.getId());
    }

    @Override
    public List<User> getFriends(long userId) {
        return jdbcTemplate.query(GET_FRIENDS_OF_USER, new UserMapper(), userId);
    }

    @Override
    public List<User> getMutualFriends(long userId, long friendId) {
        return jdbcTemplate.query(GET_MUTUAL_FRIENDS, new UserMapper(), userId, friendId);
    }
}
