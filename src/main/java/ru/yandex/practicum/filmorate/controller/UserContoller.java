package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserContoller {
    private static int id = 0;
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);
        user.setId(++id);
        users.add(user);
        log.info("User \"" + user.getLogin() + "\" was added.");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validateUser(user);
        if (!users.contains(user)) {
            throw new UserDoesntExistException("User \"" + user.getLogin() + "\" doesn't exists.");
        }
        users.remove(user);
        users.add(user);
        log.info("User \"" + user.getLogin() + "\" was updated.");
        return user;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("User's email shouldn't be empty.");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Wrong user's email input.");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("User's login shouldn't be empty.");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("User's login shouldn't contain spaces.");
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User's birthday can't be in the future.");
        }
    }
}
