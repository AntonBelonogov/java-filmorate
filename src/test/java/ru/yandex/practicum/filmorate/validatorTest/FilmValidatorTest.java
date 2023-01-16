package ru.yandex.practicum.filmorate.validatorTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidatorTest {

    List<Film> films = new ArrayList<>(List.of(
            new Film(0, "", "Tam koro4e 2 4ernix takie ......  eeeeeeee", LocalDate.of(2000, 1, 25), 1000),
            new Film(0, "Movie43", "Tam koro4e 2 4ernix takie ......  eeeeeeee", LocalDate.of(2000, 1, 25), 1000),
            new Film(0, "Movie43", "Tam koro4e 2 4ernix takie ......  eeeeeeee", LocalDate.of(1890, 1, 25), 1000),
            new Film(0, "Movie43", "Tam koro4e 2 4ernix takie ......  eeeeeeee", LocalDate.of(2000, 1, 25), -1000)
            ));

    public Film get200LengthString() {
        String string = "";
        for (int i = 0; i < 250; i++) {
            string += "1";
        }
        films.get(1).setDescription(string);
        return films.get(1);
    }

    @Test
    public void shouldGetInvalidName() {
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.postFilmCheck(films.get(0)));
    }

    @Test
    public void shouldGetInvalidDescription() {
        Film film = get200LengthString();
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.postFilmCheck(film));
    }

    @Test
    public void shouldGetInvalidDate() {
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.postFilmCheck(films.get(2)));
    }

    @Test
    public void shouldGetInvalidDuration() {
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.postFilmCheck(films.get(3)));
    }
}
