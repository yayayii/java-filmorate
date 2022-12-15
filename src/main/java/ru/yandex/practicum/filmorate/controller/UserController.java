package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static int id = 0;
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(++id);
        users.add(user);
        log.info("User \"" + user.getLogin() + "\" was added.");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
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
