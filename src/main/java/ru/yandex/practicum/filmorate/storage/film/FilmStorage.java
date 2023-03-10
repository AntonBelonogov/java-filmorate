package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilm(Integer filmId);
    Boolean isFilmExists(Integer id);
    Boolean sendLike(Integer filmId, Integer userId);
    Boolean deleteLike(Integer filmId, Integer userId);
    List<Film> getMostPopularFilm(Integer limit);
}
