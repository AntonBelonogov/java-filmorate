package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUser(Integer id) {
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY" +
                " FROM USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{"user_id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Boolean isUserExists(Integer id) {
        final String sqlQuery = "SELECT EXISTS(SELECT * FROM users WHERE user_id = ?)";
         return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        final String sqlQuery = "SELECT users.* FROM user_friends " +
                "INNER JOIN users ON user_friends.friend_id = users.user_id " +
                "WHERE user_friends.user_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public Boolean addFriend(Integer id, Integer friendId) {
        final String sqlQuery = "INSERT INTO user_friends (user_id, friend_id, is_conformed) VALUES (?, ?, ?)";
        if(isBackwardRequestExists(id, friendId)) {
            jdbcTemplate.update(sqlQuery, id, friendId, true);
            final String sqlQueryUpdate = "UPDATE user_friends SET " +
                    "is_conformed = true WHERE user_id = ? AND friend_id = ?";
            return jdbcTemplate.update(sqlQueryUpdate, friendId, id) > 0;
        } else {
            return jdbcTemplate.update(sqlQuery, id, friendId, false) > 0;
        }
    }
    private boolean isBackwardRequestExists(Integer id, Integer friendId) {
        final String sqlQuery = "SELECT EXISTS(SELECT * FROM user_friends WHERE user_id = ? AND friend_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, friendId, id));
    }

    @Override
    public Boolean deleteFriend(Integer id, Integer friendId) {
        final String sqlQuery = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
        if(isBackwardRequestExists(friendId, id)) {
            jdbcTemplate.update(sqlQuery, friendId, id);
        }
        return jdbcTemplate.update(sqlQuery, id, friendId) > 0;
    }

    @Override
    public Collection<User> getMutualFriends(Integer id, Integer friendId) {
        final String sqlQuery = "SELECT * FROM (" +
                "SELECT friend_id FROM user_friends WHERE user_id = ? INTERSECT " +
                "SELECT friend_id FROM user_friends WHERE user_id = ?" +
                ") f INNER JOIN users u ON f.friend_id = u.user_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.wasNull() ? null : User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
