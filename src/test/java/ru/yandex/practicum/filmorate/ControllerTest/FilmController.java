package ru.yandex.practicum.filmorate.ControllerTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FilmController {

    @BeforeAll
    public void postTestFilms() {
        ArrayList<Film> filmArrayList = new ArrayList<>(List.of(
                new Film(0,"TestFilm Name1", "testDescription1", LocalDate.now(), 10000),
                new Film(0,"TestFilmName2", "test Description2", LocalDate.now(), -10000),
                new Film(0,"TestFilmName3", "testDescription3", LocalDate.of(1895, 12,25), 10000)
                ));

    }

    @Test
    public void shoudTestGetFilms() {

    }

}
