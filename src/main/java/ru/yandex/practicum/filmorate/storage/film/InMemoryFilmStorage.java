package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(++id);
        films.put(id, film);
        log.info("Film \"" + film.getName() + "\" was added.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();

        validateFilm(film);
        if (!films.containsKey(filmId)) {
            RuntimeException exception = new FilmDoesntExistException("Film \"" + film.getName() + "\" doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        films.put(filmId, film);
        log.info("Film \"" + film.getName() + "\" was updated.");
        return film;
    }

    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public void clearFilmStorage() {
        id = 0;
        films.clear();
        log.info("Film storage was cleared.");
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            RuntimeException exception = new ValidationException("Film's release date can't be before 28.12.1895.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
