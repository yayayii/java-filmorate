package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
        validateFilm(film);
        if (films.contains(film)) {
            throw new RuntimeException();
        }
        films.add(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validateFilm(film);
        films.add(film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            throw new RuntimeException("Film's name shouldn't be empty.");
        }

        if (film.getDescription().length() > 200) {
            throw new RuntimeException("Film's description shouldn't contain more than 200 symbols.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new RuntimeException("Film's release date can't be before 28.12.1895.");
        }

        if (film.getDuration() < 0) {
            throw new RuntimeException("Film's duration can't be negative.");
        }
    }
}
