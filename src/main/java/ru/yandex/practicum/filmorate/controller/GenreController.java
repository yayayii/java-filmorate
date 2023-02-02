package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityDoesntExistException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        Genre genre = genreService.getGenre(id);
        if (genre == null) {
            RuntimeException exception = new EntityDoesntExistException("Genre with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return genre;
    }
    @GetMapping
    public Set<Genre> getGenres() {
        return genreService.getGenres();
    }
}
