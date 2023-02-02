package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityDoesntExistException;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

@Slf4j
@Component
public class GenreValidator {
    private final GenreStorage genreStorage;

    //inMemoryGenreStorage / genreDbStorage
    public GenreValidator(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public void validateGenreIds(int... ids) {
        for (int id : ids) {
            if (!genreStorage.getGenres().containsKey(id)) {
                RuntimeException exception = new EntityDoesntExistException("Genre with id=" + id + " doesn't exists.");
                log.warn(exception.getMessage());
                throw exception;
            }
        }
    }
}
