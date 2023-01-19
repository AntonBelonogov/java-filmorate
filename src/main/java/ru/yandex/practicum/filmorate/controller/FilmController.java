package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов: {}", films.toString());
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(id++);
        films.add(film);
        log.info("Фильм добавлен название: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.contains(film)) {
            throw new ValidationException("Такой записи нет");
        }
        films.remove(film);
        films.add(film);
        log.info("Фильм обнавлен имя: {}", film.getName());
        return film;
    }
}
