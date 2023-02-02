package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.validation.GenreValidator;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreValidator genreValidator;
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        genreValidator.validateGenreIds(id);
        return genreService.getGenre(id);
    }
    @GetMapping
    public Collection<Genre> getGenres() {
        return genreService.getGenres().values();
    }
}
