package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> getUsers();
    User getUser(Integer id);
    User addUser(User user);
    User updateUser(User user);
    Boolean isUserExists(Integer id);
    List<User> getUserFriends(Integer userId);
    Boolean addFriend(Integer id, Integer friendId);
    Boolean deleteFriend(Integer id, Integer friendId);

    Collection<User> getMutualFriends(Integer id, Integer friendId);

}
