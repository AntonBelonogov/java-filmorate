package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    public Collection<User> getUserFriends(Integer id){
        User user = userStorage.getUser(id);
        List<User> userFriendList = new ArrayList<>();

        if(user.getFriendsSet() != null) {
            for (Integer friendId : user.getFriendsSet()) {
                if (userStorage.getUsers().containsKey(friendId)) {
                    userFriendList.add(userStorage.getUser(friendId));
                }
            }
            return userFriendList;
        } else
            return Collections.emptyList();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        if (user.equals(friend)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
        if (user != null && friend != null ) {
            user.addFriend(friendId);
            friend.addFriend(userId);
            userStorage.updateUser(user);
            userStorage.updateUser(friend);
        }
        return user;
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
        if (!userStorage.getUsers().containsKey(id) && !userStorage.getUsers().containsKey(otherId))
            throw new ObjectNotFoundException("Пользователь не найден");

        ArrayList<User> commonFriendList = new ArrayList<>();
        User user = userStorage.getUser(id);

        if (user.getFriendsSet() != null) {
            for (Integer friendId : user.getFriendsSet()) {
                if (userStorage.getUsers().containsKey(friendId)) {
                    commonFriendList.add(userStorage.getUser(friendId));
                }
            }
            return commonFriendList;
        } else {
            return Collections.emptyList();
        }

    }
}
