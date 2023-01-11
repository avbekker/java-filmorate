package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .name(rs.getString("USER_NAME"))
                .login(rs.getString("LOGIN"))
                .email(rs.getString("EMAIL"))
                .birthday(Objects.requireNonNull(rs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }
}
