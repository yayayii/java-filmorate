package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {
    Set<User> getUsers();
    User addUser(User user);
    User updateUser(User user);
    void clearUserCollection();
}
