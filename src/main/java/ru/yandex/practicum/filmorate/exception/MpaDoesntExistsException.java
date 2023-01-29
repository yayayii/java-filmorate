package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MpaDoesntExistsException extends RuntimeException {
    public MpaDoesntExistsException(String message) {
        super(message);
        log.warn("MpaDoesntExistsException: " + message);
    }
}