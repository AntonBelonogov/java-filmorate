package ru.yandex.practicum.filmorate.serviceTest;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmServiceTest {
    private final FilmService filmService;
    private final Film testFilm = Film.builder()
            .id(1)
            .name("Name")
            .description("Description")
            .releaseDate(LocalDate.of(2003, 12, 3))
            .duration(150)
            .genres(null)
            .mpa(new Mpa(1, "G"))
            .build();
    private final User testUser = User.builder()
            .id(1)
            .email("Email")
            .login("Login")
            .name("Name")
            .birthday(LocalDate.of(1999, 1, 25))
            .build();

    @Test
    public void shouldAddAndGetFilmTest() {
        filmService.addFilm(testFilm);

        assertEquals(testFilm, filmService.getFilm(testFilm.getId()));
    }

    @Test
    public void shouldGetFilmsTest() {
        filmService.addFilm(testFilm);
        Collection<Film> allFilms = filmService.getFilms();

        assertEquals(1, allFilms.size());
    }


    @Test
    public void shouldUpdateFilmTest() {
        filmService.addFilm(testFilm);
        testFilm.setMpa(new Mpa(1, "G"));
        filmService.updateFilm(testFilm);

        Film film = filmService.getFilm(testFilm.getId());

        assertEquals("G", film.getMpa().getName());
    }

    @Test
    public void shouldDeleteFilmTest() {
        filmService.addFilm(testFilm);
        filmService.deleteFilm(testFilm.getId());
        Assertions.assertThatThrownBy(() -> filmService.getFilm(testFilm.getId())).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    public void shouldUpdateFilmNotFound() {
        testFilm.setId(1000);
        Assertions.assertThatThrownBy(() -> filmService.updateFilm(testFilm)).isInstanceOf(ObjectNotFoundException.class);
    }
}