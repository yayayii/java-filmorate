package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Component
public class UserValidator {
    private final UserStorage userStorage;

    public UserValidator(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void validateUserIds(int... ids) {
        for (int id : ids) {
            if (!userStorage.getUsers().containsKey(id)) {
                RuntimeException exception = new UserDoesntExistException("User with id=" + id + " doesn't exists.");
                log.warn(exception.getMessage());
                throw exception;
            }
        }
    }

    public void validateUserLogin(User user) {
        if (user.getLogin().contains(" ")) {
            RuntimeException exception = new ValidationException("User's login shouldn't contain spaces.");
            log.warn(exception.getMessage());
            throw exception;
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
