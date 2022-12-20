package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UserDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private static int id = 0;
    private final Set<User> users = new HashSet<>();

    @Override
    public Set<User> getUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        user.setId(++id);
        users.add(user);
        log.info("User \"" + user.getLogin() + "\" was added.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if (!users.contains(user)) {
            RuntimeException exception = new UserDoesntExistException("User \"" + user.getLogin() + "\" doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        users.remove(user);
        users.add(user);
        log.info("User \"" + user.getLogin() + "\" was updated.");
        return user;
    }

    @Override
    public void clearUserCollection() {
        id = 0;
        users.clear();
        log.info("User set was cleared.");
    }

    private void validateUser(User user) {
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
