package ru.yandex.practicum.filmorate.validatorTest;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    List<User> users = new ArrayList<>(List.of(
       new User(0, "testemail.com", "testlogin", "testName", LocalDate.of(2000,2,25)),
       new User(0, "teste@mail.com", "test login", "testName", LocalDate.of(2000,2,25)),
       new User(0, "teste@mail.com", "testlogin", "testName", LocalDate.of(3000,2,25)),
       new User(0, "teste@mail.com", "testlogin", "", LocalDate.of(2000,2,25))
    ));

    @Test
    public void shouldGetInvalidEmail() {
        Throwable exception = assertThrows(ValidationException.class, () -> UserValidator.userCheck(users.get(0)));
    }

    @Test
    public void shouldGetInvalidLogin() {
        Throwable exception2 = assertThrows(ValidationException.class, () -> UserValidator.userCheck(users.get(1)));
    }

    @Test
    public void shouldGetInvalidDate() {
        Throwable exception3 = assertThrows(ValidationException.class, () -> UserValidator.userCheck(users.get(2)));
        User user = users.get(3);
        UserValidator.userCheck(users.get(3));
        assertEquals(user.getLogin(), users.get(3).getName());
    }

    @Test
    public void shouldGetNameFromLogin() {
        User user = users.get(3);
        UserValidator.userCheck(users.get(3));
        assertEquals(user.getLogin(), users.get(3).getName());
    }

}
