package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getFilmCounted(@RequestParam(required = false, defaultValue = "10") Integer count) {
        if(count <= 0)
            throw new IllegalArgumentException("Параметер count не может быть меньше 0");
        return filmService.getCountedFilmList(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
       return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film userPostLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.sendLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film userDeleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.deleteLike(id,userId);
    }
}
