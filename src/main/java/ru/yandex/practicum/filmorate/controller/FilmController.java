package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (films.contains(film)) {
            throw new RuntimeException();
        }
        films.add(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        films.add(film);
        return film;
    }
}
