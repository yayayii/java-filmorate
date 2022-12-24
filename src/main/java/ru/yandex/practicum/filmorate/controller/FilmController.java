package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final FilmValidator filmValidator;
    private final UserValidator userValidator;

    //storage mapping
    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        filmValidator.validateFilmIds(id);
        return filmStorage.getFilm(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        filmValidator.validateFilmDate(film);
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmValidator.validateFilmIds(film.getId());
        filmValidator.validateFilmDate(film);
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public void clearFilmStorage() {
        filmStorage.clearFilmStorage();
    }

    //service mapping
    @GetMapping("/popular")
    public Set<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) int count) {
        filmValidator.validateFilmsCount(count);
        if (count > filmStorage.getFilms().size()) {
            count = filmStorage.getFilms().size();
        }
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmValidator.validateFilmIds(id);
        userValidator.validateUserIds(userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmValidator.validateFilmIds(id);
        userValidator.validateUserIds(userId);
        filmService.removeLike(id, userId);
    }
}
