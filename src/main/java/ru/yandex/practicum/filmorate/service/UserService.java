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
        user.addFriend(friendId);
        friend.addFriend(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);

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
        if (!userStorage.getUsers().contains(id) && !userStorage.getUsers().contains(otherId))
            throw new ObjectNotFoundException("Пользователь не найден");
        User user = userStorage.getUser(id);
        User user2 = userStorage.getUser(otherId);
        List<User> commonUserList = new ArrayList<>();

        for (Integer firstUserFriends : user.getFriendsSet()){
            for (Integer secondUserFriends : user2.getFriendsSet()) {
                if (firstUserFriends.equals(secondUserFriends)) {
                    commonUserList.add(userStorage.getUser(firstUserFriends));
                }
            }
        }
        return commonUserList;
    }
}
