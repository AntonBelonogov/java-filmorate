package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Map<Integer, User> getUsers();
    User getUser(Integer id);
    User addUser(User user);
    User updateUser(User user);

    Collection<Integer> getUserFriends(Integer id);
}
