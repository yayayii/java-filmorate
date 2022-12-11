package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserContoller {
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);
        if (users.contains(user)) {
            throw new RuntimeException();
        }
        users.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validateUser(user);
        users.add(user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new RuntimeException("User's email shouldn't be empty.");
        }
        if (!user.getEmail().contains("@")) {
            throw new RuntimeException("Wrong user's email input.");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new RuntimeException("User's login shouldn't be empty.");
        }
        if (user.getLogin().contains(" ")) {
            throw new RuntimeException("User's login shouldn't contain spaces.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException("User's birthday can't be in the future.");
        }
    }
}
