package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres() {
        final String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre getGenre(int id) {
        final String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }
    public Map<Integer, List<Genre>> getFilmsGenres() {
        final String sqlQuery = "SELECT * FROM FILM_GENRE JOIN GENRE ON FILM_GENRE.GENRE_ID = GENRE.GENRE_ID";
        List<FilmGenre> filmGenreList = jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre);
        Map<Integer, List<Genre>> map = new HashMap<>();
        for (FilmGenre filmGenre : filmGenreList) {
            if (!filmGenreList.contains(filmGenre.getFilmId())) {
                map.put(filmGenre.getFilmId(), new ArrayList<>());
            }
            map.get(filmGenre.getFilmId()).add(filmGenre.getGenre());
        }
        return map;
    }
    public Boolean isGenreExists(Integer id) {
        final String sqlQuery = "SELECT EXISTS(SELECT * FROM genre WHERE genre_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    private FilmGenre mapRowToFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();

        return FilmGenre.builder()
                .filmId(resultSet.getInt("film_id"))
                .genre(genre)
                .build();

    }
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
