package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int id = 0;
    Set<Film> films = new HashSet<>(Set.of(new Film(id++,"TestFilmName", "testDescription", LocalDate.now(), 10000)));

    @GetMapping
    public Set<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов: {}", films.toString());
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(id++);
        if (FilmValidator.postFilmCheck(film)) {
            films.add(film);
            log.info("Фильм добавлен название: {}", film.getName());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (FilmValidator.putFilmCheck(film, films)) {
            films.remove(film);
            films.add(film);
            log.info("Фильм обнавлен имя: {}", film.getName());
        }
        return film;
    }
}
