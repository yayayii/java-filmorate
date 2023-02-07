package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityDoesntExistException extends RuntimeException {
    public EntityDoesntExistException(String message) {
        super(message);
        log.warn("EntityDoesntExistException: " + message);
    }
}
