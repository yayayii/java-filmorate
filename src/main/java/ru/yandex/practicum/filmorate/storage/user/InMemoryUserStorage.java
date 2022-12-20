package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UserDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private static int id = 0;
    private final Map<Integer, User> users;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        user.setId(++id);
        users.put(id, user);
        log.info("User \"" + user.getLogin() + "\" was added.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();

        validateUser(user);
        if (!users.containsKey(userId)) {
            RuntimeException exception = new UserDoesntExistException("User \"" + user.getLogin() + "\" doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        users.put(userId, user);
        log.info("User \"" + user.getLogin() + "\" was updated.");
        return user;
    }

    @Override
    public void clearUserStorage() {
        id = 0;
        users.clear();
        log.info("User storage was cleared.");
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
