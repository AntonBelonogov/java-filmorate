package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private int id = 1;
    private final HashMap<Integer,User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос на получение списка пользователей: {}", users.values());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не нейден");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null|| user.getName().equals("") || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен email: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("Такого пользователя нет");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обнавлен email: {}", user);
        return user;
    }

    @Override
    public Boolean deleteUser(Integer id) {
        return null;
    }

    @Override
    public Boolean isUserExists(Integer id) {
        return null;
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        return null;
    }

    @Override
    public Boolean addFriend(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public Boolean deleteFriend(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public Collection<User> getMutualFriends(Integer id, Integer friendId) {
        return null;
    }
}
