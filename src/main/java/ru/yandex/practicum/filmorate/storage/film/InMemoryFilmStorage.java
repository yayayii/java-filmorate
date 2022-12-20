package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.FilmDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static int id = 0;

    private final Set<Film> films = new HashSet<>();

    @Override
    public Set<Film> getFilms() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(++id);
        films.add(film);
        log.info("Film \"" + film.getName() + "\" was added.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        if (!films.contains(film)) {
            RuntimeException exception = new FilmDoesntExistException("Film \"" + film.getName() + "\" doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        films.remove(film);
        films.add(film);
        log.info("Film \"" + film.getName() + "\" was updated.");
        return film;
    }

    @Override
    public void clearFilmCollection() {
        id = 0;
        films.clear();
        log.info("Film set was cleared.");
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            RuntimeException exception = new ValidationException("Film's release date can't be before 28.12.1895.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
