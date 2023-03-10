package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;
    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getMpa() {
        return mpaDbStorage.getMpa();
    }

    public Mpa getMpa(Integer id) {
        if(!mpaDbStorage.isMpaExists(id)) {
            throw new ObjectNotFoundException("Mpa id="+ id + " not found.");
        }
        return mpaDbStorage.getMpa(id);
    }

}
