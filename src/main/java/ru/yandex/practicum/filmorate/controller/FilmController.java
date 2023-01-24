package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmDoesntExistException;
import ru.yandex.practicum.filmorate.model.Film;
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
        Film film = filmService.getFilm(id);
        if (film == null) {
            RuntimeException exception = new FilmDoesntExistException("Film with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return film;
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
}
