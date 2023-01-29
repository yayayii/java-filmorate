package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenreDoesntExistsException extends RuntimeException {
    public GenreDoesntExistsException(String message) {
        super(message);
        log.warn("GenreDoesntExistsException: " + message);
    }
}
