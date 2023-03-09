package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(Integer id) {
        if (!userStorage.isUserExists(id)) {
            throw new ObjectNotFoundException("User id not found to update.");
        }
        return userStorage.getUser(id);
    }

    public Collection<User> getUserFriends(Integer id){
        User user = userStorage.getUser(id);
        ArrayList<User> commonFriendList = new ArrayList<>();
        if (user.getFriendsSet() != null) {
            for (Integer friendId : user.getFriendsSet()) {
                if (userStorage.getUsers().contains(friendId)) {
                    commonFriendList.add(userStorage.getUser(friendId));
                }
            }
            return commonFriendList;
        }
        return Collections.emptyList();
    }

    public User addUser(User user) {
        if (user.getName() == null|| user.getName().equals("") || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (!userStorage.isUserExists(user.getId())) {
            throw new ObjectNotFoundException("User id not found to update.");
        }
        return userStorage.updateUser(user);
    }

    public Boolean addFriend(Integer userId, Integer friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (!user.getFriendsSet().contains(friendId)) {
            throw new RuntimeException("Такого пользователя нет в друзьях");
        }
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        return user;
    }

    public Collection<User> getMutualFriends(Integer id, Integer otherId) {
        return userStorage.getMutualFriends(id, otherId);
    }
}
