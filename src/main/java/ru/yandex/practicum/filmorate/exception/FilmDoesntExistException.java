package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilmDoesntExistException extends RuntimeException {
    public FilmDoesntExistException(String message) {
        super(message);
        log.warn("FilmDoesntExistException: " + message);
    }
}
