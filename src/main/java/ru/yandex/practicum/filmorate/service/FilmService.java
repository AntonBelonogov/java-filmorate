package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
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
        if (!filmStorage.getFilms().contains(filmId) && !userStorage.getUsers().contains(userId))
            throw new ObjectNotFoundException("Фильма или пользователя не существует");
        filmStorage.getFilm(filmId).getUsersLikes().add(userId);
        return filmStorage.getFilm(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (!userStorage.getUsers().contains(userId))
            throw new ObjectNotFoundException("Такого пользователя нет");
        filmStorage.getFilm(filmId).getUsersLikes().remove(userId);
        return filmStorage.getFilm(filmId);
    }

    public List<Film> getCountedFilmList(Integer count) {
        return filmStorage.getFilms()
                .stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
