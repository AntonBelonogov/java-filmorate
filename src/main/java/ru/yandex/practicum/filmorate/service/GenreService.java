package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.Map;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;
    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }
    public List<Genre> getGenres() {
        return genreDbStorage.getGenres();
    }

    public Map<Integer, List<Genre>> getFilmsGenres() {
        return genreDbStorage.getFilmsGenres();
    }

    public Genre getGenre(Integer id) {
        if(!genreDbStorage.isGenreExists(id)) {
            throw new ObjectNotFoundException("Genre id=" + id + " not found.");
        }
        return genreDbStorage.getGenre(id);
    }
}
