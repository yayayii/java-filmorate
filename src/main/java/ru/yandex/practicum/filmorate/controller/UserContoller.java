package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

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
        if (users.contains(user)) {
            throw new RuntimeException();
        }
        users.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        users.add(user);
        return user;
    }
}
