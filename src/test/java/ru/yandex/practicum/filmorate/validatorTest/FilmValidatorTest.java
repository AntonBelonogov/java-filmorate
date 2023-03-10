package ru.yandex.practicum.filmorate.validatorTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidatorTest {

    List<Film> films = new ArrayList<>();

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
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.filmCheck(films.get(0)));
    }

    @Test
    public void shouldGetInvalidDescription() {
        Film film = get200LengthString();
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.filmCheck(film));
    }

    @Test
    public void shouldGetInvalidDate() {
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.filmCheck(films.get(2)));
    }

    @Test
    public void shouldGetInvalidDuration() {
        Throwable exception = assertThrows(ValidationException.class, () -> FilmValidator.filmCheck(films.get(3)));
    }
}
