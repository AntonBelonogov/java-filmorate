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
        if (!filmStorage.isFilmExists(film.getId())) {
            throw new ObjectNotFoundException("Film id= " + film.getId() + "not found.");
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        if (!filmStorage.isFilmExists(id)) {
            throw new ObjectNotFoundException("Film id= " + id + "not found.");
        }
        return filmStorage.getFilm(id);
    }

    public Boolean sendLike(Integer filmId, Integer userId) {
        if (!filmStorage.isFilmExists(filmId) && !userStorage.isUserExists(userId)) {
            throw new ObjectNotFoundException("User and/or film not found.");
        }
        return filmStorage.sendLike(filmId, userId);
    }

    public Boolean deleteLike(Integer filmId, Integer userId) {
        if (!filmStorage.isFilmExists(filmId) && !userStorage.isUserExists(userId)) {
            throw new ObjectNotFoundException("User and/or film not found.");
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getCountedFilmList(Integer count) {
        return filmStorage.getFilms()
                .stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
