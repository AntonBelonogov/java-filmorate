package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private int id = 1;
    private final Map<Integer,Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов: {}", films.values());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен название: {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsValue(film)) {
            throw new ObjectNotFoundException("Такой записи нет");
        }
        films.put(film.getId(), film);
        log.info("Фильм обнавлен имя: {}", film.getName());
        return film;
    }

    @Override
    public Film getFilm(Integer filmId) {
        if(!films.containsKey(filmId)) {
            throw new ObjectNotFoundException("Нет такого фильма");
        }
        return films.get(filmId);
    }
}
