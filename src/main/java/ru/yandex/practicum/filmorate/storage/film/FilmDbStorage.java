package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Film> getFilms() {
        final String sqlQuery = "SELECT * FROM FILM";

        List<Film> filmList = new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToFilm));

        for(Film film : filmList) {
            film.setGenres(getGenres(film.getId()));
        }

        return filmList;
    }

    @Override
    public Film addFilm(Film film) {
        final String sqlQuery = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        film.setMpa(getMpa(film.getMpa().getId()));

        final String sqlGenreQuery = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : new HashSet<>(film.getGenres())) {
                jdbcTemplate.update(sqlGenreQuery, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String sqlQuery = "UPDATE film SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            List<Genre> genreList = new ArrayList<>();
            final String sqlDeleteQuery = "DELETE FROM film_genre WHERE film_id = ?";
            final String sqlInsertQuery = "INSERT INTO film_genre (film_id, genre_id) " +
                    "VALUES (?, ?)";
            jdbcTemplate.update(sqlDeleteQuery, film.getId());
            for (Genre genre : new HashSet<>(film.getGenres())) {
                jdbcTemplate.update(sqlInsertQuery, film.getId(), genre.getId());
                genreList.add(genre);
            }
            genreList.sort((o1, o2) -> {
                return o1.getId() - o2.getId();
            });
            film.setGenres(genreList);
        }
        return film;
    }

    @Override
    public Film deleteFilm(Integer film_id) {
        Film film = getFilm(film_id);
        final String sqlFilmGenreDelQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlFilmGenreDelQuery, film_id);
        final String sqlUserLikesDelQuery = "DELETE FROM USER_LIKES WHERE film_id = ?";
        jdbcTemplate.update(sqlUserLikesDelQuery, film_id);
        final String sqlQuery = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film_id);
        return film;
    }

    @Override
    public Film getFilm(Integer filmId) {
        final String sqlQuery = "SELECT * FROM FILM WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }

    @Override
    public Boolean isFilmExists(Integer id) {
        final String sqlQuery = "SELECT EXISTS(SELECT * FROM film WHERE film_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    @Override
    public Boolean sendLike(Integer filmId, Integer userId) {
        final String sqlQuery = "INSERT INTO user_likes (film_id, user_id) VALUES (?, ?)";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public Boolean deleteLike(Integer filmId, Integer userId) {
        final String sqlQuery = "DELETE FROM user_likes WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Film> getMostPopularFilm(Integer limit) {
        final String sqlQuery = "SELECT * FROM film " +
                "LEFT JOIN user_likes ON film.film_id = user_likes.film_id " +
                "GROUP BY film.film_id " +
                "ORDER BY COUNT(user_likes.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, limit);
    }

    private List<Genre> getGenres(int film_id) {
        final String sqlQuery = "SELECT * FROM GENRE " +
                "LEFT JOIN film_genre ON genre.genre_id = film_genre.genre_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, film_id);
    }

    private Mpa getMpa(int id) {
        final String sqlQuery = "SELECT * FROM MPA WHERE ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }
    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .genres(getGenres(resultSet.getInt("FILM_ID")))
                .mpa(getMpa(resultSet.getInt("MPA")))
                .build();
    }
}
