package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator {
    private final FilmStorage filmStorage;


    //inMemoryFilmStorage / filmDbStorage
    public FilmValidator(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    public void validateFilmIds(int... ids) {
        for (int id : ids) {
            if (!filmStorage.getFilms().containsKey(id)) {
                RuntimeException exception = new EntityDoesntExistException("Film with id=" + id + " doesn't exists.");
                log.warn(exception.getMessage());
                throw exception;
            }
        }
    }

    public void validateFilmDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            RuntimeException exception = new ValidationException("Film's release date can't be before 28.12.1895.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    public void validateFilmsCount(int count) {
        if (count < 1) {
            RuntimeException exception = new IllegalArgumentException("Films count should be positive.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
