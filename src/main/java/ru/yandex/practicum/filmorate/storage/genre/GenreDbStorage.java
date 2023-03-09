package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
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

    public Collection<Genre> getGenres() {
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
            //map.get(filmGenre.getFilmId()).add(filmGenre.getGenreId());
        }
        return null;
    }

    private FilmGenre mapRowToFilmGenre(ResultSet resultSet, int rowNum) throws  SQLException {
        return FilmGenre.builder()
                .filmGenreId(resultSet.getInt("film_genre_id"))
                .filmId(resultSet.getInt("film_id"))
                .genreId(resultSet.getInt("genre_id"))
                .build();

    }
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
