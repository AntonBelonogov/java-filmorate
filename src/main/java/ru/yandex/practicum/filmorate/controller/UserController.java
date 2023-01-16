package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private int id = 0;
    Set<User> users = new HashSet<>(Set.of(new User(id++, "testemail@gmail.com", "testLogin", "testName", LocalDate.now())));


    @GetMapping
    public Set<User> getFilms() {
        log.info("Получен запрос на получение списка фильмов: {}", users.toString());
        return users;
    }

    @PostMapping
    public User addFilm(@Valid @RequestBody User user) {
        user.setId(id++);
        if(UserValidator.postUserCheck(user)) {
            users.add(user);
            log.info("Пользователь добавлен email: {}", user.getEmail());
        }
        return user;
    }

    @PutMapping
    public User updateFilm(@Valid @RequestBody User user) {
        if (UserValidator.putUserCheck(user, users)) {
            users.remove(user);
            users.add(user);
            log.info("Пользователь обнавлен email: {}", user.getEmail());
        }
        return user;
    }
}