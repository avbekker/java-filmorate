package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserStorageDao implements UserDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final static String CREATE = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
    private final static String DELETE = "DELETE FROM USERS WHERE USER_ID = ?";
    private final static String UPDATE = "UPDATE USERS SET USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
    private final static String GET_USERS = "SELECT USER_ID FROM USERS";
    private final static String GET_BY_ID = "SELECT* FROM USERS WHERE USER_ID = ?";
    private final static String GET_FRIENDS_OF_USER = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID =?)";
    private final static String MAKE_FRIEND = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
    private final static String DELETE_FRIEND = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String GET_MUTUAL_FRIENDS = "SELECT * FROM USERS WHERE USER_ID IN " +
            "(SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID =?) AND USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID =?)";

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE, new String[] {"USER_ID"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setDate(4, Date.valueOf(user.getBirthday()));
                return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        long id = user.getId();
        return getById(id).orElseThrow(() -> new NotFoundException("Not found"));
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update(DELETE, user.getId());
    }

    @Override
    public User update(User user) {
        int id = jdbcTemplate.update(UPDATE, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());
        return getById(id).orElseThrow(() -> new NotFoundException("Not found"));
    }

    @Override
    public List<User> getAll() {
        List<Long> usersIds = jdbcTemplate.queryForList(GET_USERS, Long.class);
        List<User> result = new ArrayList<>();
        for (Long usersId : usersIds) {
            result.add(getById(usersId).orElseThrow(() -> new NotFoundException("Not found")));
        }
        return result;
    }

    @Override
    public Optional<User> getById(long id) {
        try {
            User user = jdbcTemplate.queryForObject(GET_BY_ID, userMapper, id);
            if (user == null) {
                throw new NotFoundException("Not found");
            }
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found", e);
        }
    }

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
