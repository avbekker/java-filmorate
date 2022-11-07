package ru.yandex.practicum.filmorate.controllersTests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController controller = new UserController();
    User goodUser = User.builder().id(1).email("1@mailer.com").login("goodLogin").name("goodName")
                .birthday(LocalDate.of(1990, 9, 20)).build();
    User goodUser2 = User.builder().id(2).email("2@mailer.com").login("goodLogin2").name("goodName2")
                .birthday(LocalDate.of(1990, 9, 20)).build();

    @Test
    public void createNewUserTest() {
        controller.createUser(goodUser);
        assertEquals(1, controller.getUsers().size());
        controller.createUser(goodUser2);
        assertEquals(2, controller.getUsers().size());
    }
    @Test
    public void updateUserTest(){
        controller.createUser(goodUser);
        assertEquals(1, controller.getUsers().size());
        User goodNewUser = User.builder().id(1).email("1@mailer.com").login("goodLogin").name("updateName")
                .birthday(LocalDate.of(1990, 9, 20)).build();
        controller.updateUser(goodNewUser);
        assertEquals(1, controller.getUsers().size());
        User goodNewUser1 = User.builder().id(1).email("new@mailer.com").login("goodLogin").name("updateName")
                .birthday(LocalDate.of(1990, 9, 20)).build();
        controller.updateUser(goodNewUser1);
        assertEquals(1, controller.getUsers().size());
        User goodNewUser2 = User.builder().id(1).email("new@mailer.com").login("updateLogin").name("updateName")
                .birthday(LocalDate.of(1990, 9, 20)).build();
        controller.updateUser(goodNewUser2);
        assertEquals(1, controller.getUsers().size());
        User goodNewUser3 = User.builder().id(1).email("new@mailer.com").login("updateLogin").name("updateName")
                .birthday(LocalDate.of(2000, 9, 20)).build();
        controller.updateUser(goodNewUser3);
        assertEquals(1, controller.getUsers().size());
    }
    @Test
    public void shouldFailCreationWithBadEmail(){
        User userWithBadEmail = User.builder().id(1).login("Login").name("Name").birthday(LocalDate.of(1990, 9, 20))
                .email("1mailer.com")
                .build();
        assertThrows(ValidationException.class, () -> controller.createUser(userWithBadEmail));
    }
    @Test
    public void shouldFailCreationWithBadLogin(){
        User userWithBadLogin = User.builder().id(1).email("1@mailer.com")
                .login("Login with spaces")
                .name("Name").birthday(LocalDate.of(1990, 9, 20)).build();
        assertThrows(ValidationException.class, () -> controller.createUser(userWithBadLogin));
    }
    @Test
    public void ShouldFailCreationWithBadBirthday() {
        User userWithBadBirthday = User.builder().id(1).email("1@mailer.com").login("Login").name("Name")
                .birthday(LocalDate.of(2023, 9, 20))
                .build();
        assertThrows(ValidationException.class, () -> controller.createUser(userWithBadBirthday));
    }
    @Test
    public void ShouldFailCreationWithBlankName() {
        User userWithBBlankName = User.builder().id(1).email("1@mailer.com").login("Login")
                .name("")
                .birthday(LocalDate.of(1990, 9, 20)).build();
        controller.createUser(userWithBBlankName);
        assertEquals(userWithBBlankName.getLogin(), userWithBBlankName.getName());
    }
}
