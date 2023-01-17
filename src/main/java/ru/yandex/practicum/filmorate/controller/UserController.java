package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private int id = 1;
    Set<User> users = new HashSet<>();
    @GetMapping
    public Set<User> getFilms() {
        log.info("Получен запрос на получение списка фильмов: {}", users.toString());
        return users;
    }

    @PostMapping
    public User addFilm(@Valid @RequestBody User user) {
        user.setId(id++);
        if(UserValidator.userCheck(user)) {
            users.add(user);
            log.info("Пользователь добавлен email: {}", user.getEmail());
        }
        return user;
    }

    @PutMapping
    public User updateFilm(@Valid @RequestBody User user) {
        if (UserValidator.userCheck(user)) {
            users.remove(user);
            users.add(user);
            log.info("Пользователь обнавлен email: {}", user.getEmail());
        }
        return user;
    }
}
