package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static int id = 0;
    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(++id);
        films.add(film);
        log.info("Film \"" + film.getName() + "\" was added.");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateFilm(film);
        if (!films.contains(film)) {
            throw new FilmDoesntExistException("Film \"" + film.getName() + "\" doesn't exists.");
        }
        films.remove(film);
        films.add(film);
        log.info("Film \"" + film.getName() + "\" was updated.");
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film's description shouldn't contain more than 200 symbols.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film's release date can't be before 28.12.1895.");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Film's duration can't be negative.");
        }
    }
}
