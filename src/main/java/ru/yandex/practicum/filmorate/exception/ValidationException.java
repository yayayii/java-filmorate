package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
        log.warn("ValidationException: " + message);
    }
}
