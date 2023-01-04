package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserStorageDao implements UserDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final static String CREATE = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
    private final static String GET_LAST_ADDED = "SELECT USER_ID FROM USERS ORDER BY USER_ID DESC LIMIT 1";
    private final static String DELETE = "DELETE FROM USERS WHERE USER_ID = ?";
    private final static String UPDATE = "UPDATE USERS SET USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
    private final static String GET_USERS = "SELECT USER_ID FROM USERS";
    private final static String GET_BY_ID = "SELECT* FROM USERS WHERE USER_ID = ?";
    private final static String GET_FRIENDS_OF_USER = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
    private final static String MAKE_FRIEND = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
    private final static String DELETE_FRIEND = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";

    @Override
    public User create(User user) {
        jdbcTemplate.update(CREATE, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        Integer id = jdbcTemplate.queryForObject(GET_LAST_ADDED, Integer.class);
        if (id == null) {
            throw new NotFoundException("Not found");
        }
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
    public List<User> getUsers() {
        List<Long> usersIds = jdbcTemplate.queryForList(GET_USERS, Long.class);
        List<User> result = new ArrayList<>();
        for (Long usersId : usersIds) {
            result.add(getById(usersId).orElseThrow(() -> new NotFoundException("Not found")));
        }
        return result;
    }

    @Override
    public Optional<User> getById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(GET_BY_ID, id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("USER_ID"),
                                                    userRows.getString("LOGIN"));
            List<Long> friends = jdbcTemplate.queryForList(GET_FRIENDS_OF_USER, Long.class, id);
            Set<Long> fr = new HashSet<>(friends);
            User user = User.builder()
                    .id(userRows.getLong("USER_ID"))
                    .name(userRows.getString("USER_NAME"))
                    .login(userRows.getString("LOGIN"))
                    .email(userRows.getString("EMAIL"))
                    .birthday(Objects.requireNonNull(userRows.getDate("BIRTHDAY")).toLocalDate())
                    .friends(fr)
                        .build();
            return Optional.of(user);
        }
        log.info("Пользователь с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    @Override
    public void makeFriend(User user, User friend) {
        jdbcTemplate.update(MAKE_FRIEND, user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        jdbcTemplate.update(DELETE_FRIEND, user.getId(), friend.getId());
    }
}
