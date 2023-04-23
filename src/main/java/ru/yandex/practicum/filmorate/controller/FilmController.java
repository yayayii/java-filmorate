package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmValidator filmValidator;
    private final UserValidator userValidator;


    //films
    //create
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        filmValidator.validateFilmDate(film);
        return filmService.addFilm(film);
    }

    //read
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        filmValidator.validateFilmIds(id);
        return filmService.getFilm(id);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms().values();
    }

    //update
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmValidator.validateFilmIds(film.getId());
        filmValidator.validateFilmDate(film);
        return filmService.updateFilm(film);
    }

    //delete
    @DeleteMapping
    public void clearFilmStorage() {
        filmService.clearFilmStorage();
    }

    //likes
    //create
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmValidator.validateFilmIds(id);
        userValidator.validateUserIds(userId);
        filmService.addLike(id, userId);
    }

    //read
    @GetMapping("/{id}/like")
    public Set<Integer> getLikedUsersIds(@PathVariable int id) {
        filmValidator.validateFilmIds(id);
        return filmService.getLikedUsersIds(id);
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) int count) {
        filmValidator.validateFilmsCount(count);
        if (count > filmService.getFilms().size()) {
            count = filmService.getFilms().size();
        }
        return filmService.getPopularFilms(count);
    }

    //update
    //delete
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmValidator.validateFilmIds(id);
        userValidator.validateUserIds(userId);
        filmService.removeLike(id, userId);
    }
}
