package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Set;

public class UserValidator {
    public static boolean postUserCheck(User user) {
        if (!emailCheck(user.getEmail())) {
            throw new ValidationException("Электронная почта неверна.");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем.");
        } else if (user.getName().equals("") || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
    }

    private static boolean emailCheck(String email) {
        return email.matches("^[a-z0-9]+[\\._]?[a-z0-9]+[@]\\w+[.]\\w{2,3}$");
    }

    public static boolean putUserCheck(User user, Set<User> userSet) {
        if (postUserCheck(user)) {
            return userSet.contains(user);
        } else
            throw new ValidationException("Нельзя обновить пользователя");
    }
}