package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService, UserValidator userValidator) {
        this.userStorage = userStorage;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    //storage mapping
    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        userValidator.validateUserIds(id);
        return userStorage.getUser(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userValidator.validateUserLogin(user);
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        userValidator.validateUserIds(user.getId());
        userValidator.validateUserLogin(user);
        return userStorage.updateUser(user);
    }

    @DeleteMapping
    public void clearUserStorage() {
        userStorage.clearUserStorage();
    }

    //service mapping
    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable int id) {
        userValidator.validateUserIds(id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getFriends(@PathVariable int id, @PathVariable int otherId) {
        userValidator.validateUserIds(id, otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userValidator.validateUserIds(id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userValidator.validateUserIds(id, friendId);
        userService.deleteFriend(id, friendId);
    }
}
