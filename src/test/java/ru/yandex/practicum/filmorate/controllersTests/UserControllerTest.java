package ru.yandex.practicum.filmorate.controllersTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final UserStorageDao dbStorage;

    User goodUser = User.builder()
            .id(1)
            .email("1@mailer.com")
            .login("goodLogin")
            .name("goodName")
            .birthday(LocalDate.of(1990, 9, 20))
            .build();
    User goodUser2 = User.builder()
            .id(2)
            .email("2@mailer.com")
            .login("goodLogin2")
            .name("goodName2")
            .birthday(LocalDate.of(1990, 9, 20))
            .build();

    @Test
    public void createUserTest(){
        assertEquals(1, goodUser.getId());
    }

    @Test
    public void updateUserTest(){
        dbStorage.update(
                User.builder()
                        .id(1)
                        .email("1@mailer.com")
                        .login("newGoodLogin")
                        .name("goodName")
                        .birthday(LocalDate.of(1990, 9, 20))
                        .build()
        );
        assertEquals(dbStorage.getById(1).get().getLogin(), "newGoodLogin");
    }

    @Test
    public void getAllUsers(){
        dbStorage.create(goodUser);
        dbStorage.create(goodUser2);
        List<User> users = dbStorage.getAll();
        assertEquals(2, users.size());
    }

    @Test
    public void getUserById(){
        assertThat(dbStorage.getById(1))
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
    }
}
