package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.GenreDoesntExistsException;
import ru.yandex.practicum.filmorate.exception.MpaDoesntExistsException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
public class FilmController {
    private final FilmService filmService;
    private final FilmValidator filmValidator;
    private final UserValidator userValidator;

    //films
    //create
    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        filmValidator.validateFilmDate(film);
        return filmService.addFilm(film);
    }
    //read
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        filmValidator.validateFilmIds(id);
        return filmService.getFilm(id);
    }

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return filmService.getFilms().values();
    }

    @GetMapping("/films/popular")
    public Set<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) int count) {
        filmValidator.validateFilmsCount(count);
        if (count > filmService.getFilms().size()) {
            count = filmService.getFilms().size();
        }
        return filmService.getPopularFilms(count);
    }
    //update
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmValidator.validateFilmIds(film.getId());
        filmValidator.validateFilmDate(film);
        return filmService.updateFilm(film);
    }
    //delete
    @DeleteMapping("/films")
    public void clearFilmStorage() {
        filmService.clearFilmStorage();
    }

    //likes
    //create
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmValidator.validateFilmIds(id);
        userValidator.validateUserIds(userId);
        filmService.addLike(id, userId);
    }
    //read
    //update
    //delete
    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmValidator.validateFilmIds(id);
        userValidator.validateUserIds(userId);
        filmService.removeLike(id, userId);
    }

    //mpa
    //create
    //read
    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        Mpa mpa = filmService.getMpa(id);
        if (mpa == null) {
            RuntimeException exception = new MpaDoesntExistsException("Mpa with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return mpa;
    }

    @GetMapping("/mpa")
    public Mpa[] getMpas() {
        return filmService.getMpas();
    }
    //update
    //delete

    //genre
    //create
    //read
    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        Genre genre = filmService.getGenre(id);
        if (genre == null) {
            RuntimeException exception = new GenreDoesntExistsException("Genre with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return genre;
    }

    @GetMapping("/genres")
    public Genre[] getGenres() {
        return filmService.getGenres();
    }
    //update
    //delete
}
