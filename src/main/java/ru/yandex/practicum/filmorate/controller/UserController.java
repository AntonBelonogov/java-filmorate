package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Boolean addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Boolean deleteUser(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.deleteFriend(id, friendId);
    }
}
