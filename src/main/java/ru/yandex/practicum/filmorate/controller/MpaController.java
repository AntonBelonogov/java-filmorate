package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;
    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }
    @GetMapping
    public List<Mpa> getMpa() {
        return mpaService.getMpa();
    }
    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable Integer id) {
        return mpaService.getMpa(id);
    }
}
