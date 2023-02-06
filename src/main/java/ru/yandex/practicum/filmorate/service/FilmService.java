package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    public Film sendLike(Integer filmId, Integer userId) {
        if (!filmStorage.getFilms().containsKey(filmId) && !userStorage.getUsers().containsKey(userId))
            throw new ObjectNotFoundException("Фильма или пользователя не существует");
        filmStorage.getFilm(filmId).getUsersLikes().add(userId);
        return filmStorage.getFilm(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (!userStorage.getUsers().containsKey(userId))
            throw new ObjectNotFoundException("Такого пользователя нет");
        filmStorage.getFilm(filmId).getUsersLikes().remove(userId);
        return filmStorage.getFilm(filmId);
    }

    public List<Film> getCountedFilmList(Integer count) {
        return filmStorage.getFilms().values()
                .stream()
                .sorted(Comparator.comparing(Film::getLikesCount))
                .limit(count)
                .collect(Collectors.toList());
    }
}
