package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserDoesntExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;
    private final UserValidator userValidator;

    //storage mapping
    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        userValidator.validateUserIds(id);
        User user = userStorage.getUser(id);
        if (user == null) {
            RuntimeException exception = new UserDoesntExistException("User with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return user;
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

    //friends mapping
    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable int id) {
        userValidator.validateUserIds(id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
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
