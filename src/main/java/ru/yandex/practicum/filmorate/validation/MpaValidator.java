package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityDoesntExistException;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

@Slf4j
@Component
public class MpaValidator {
    private final MpaStorage mpaStorage;

    //inMemoryMpaStorage / mpaDbStorage
    public MpaValidator(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public void validateMpaIds(int... ids) {
        for (int id : ids) {
            if (!mpaStorage.getMpas().containsKey(id)) {
                RuntimeException exception = new EntityDoesntExistException("Mpa with id=" + id + " doesn't exists.");
                log.warn(exception.getMessage());
                throw exception;
            }
        }
    }
}
