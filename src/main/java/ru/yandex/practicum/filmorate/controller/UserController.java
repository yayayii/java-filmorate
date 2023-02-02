package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityDoesntExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    //users
    //create
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        userValidator.validateUserLogin(user);
        return userService.addUser(user);
    }
    //read
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        userValidator.validateUserIds(id);
        User user = userService.getUser(id);
        if (user == null) {
            RuntimeException exception = new EntityDoesntExistException("User with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return user;
    }
    @GetMapping("/users")
    public Collection<User> getUsers() {
        return userService.getUsers().values();
    }
    //update
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        userValidator.validateUserIds(user.getId());
        userValidator.validateUserLogin(user);
        return userService.updateUser(user);
    }
    //delete
    @DeleteMapping("/users")
    public void clearUserStorage() {
        userService.clearUserStorage();
    }

    //friends
    //create
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userValidator.validateUserIds(id, friendId);
        userService.addFriend(id, friendId);
    }
    //read
    @GetMapping("/users/{id}/friends")
    public Set<User> getFriends(@PathVariable int id) {
        userValidator.validateUserIds(id);
        return userService.getFriends(id);
    }
    @GetMapping("/users/{id}/friends/confirmed")
    public Set<User> getConfirmedFriends(@PathVariable int id) {
        userValidator.validateUserIds(id);
        return userService.getConfirmedFriends(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        userValidator.validateUserIds(id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
    //update
    //delete
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userValidator.validateUserIds(id, friendId);
        userService.deleteFriend(id, friendId);
    }
}
