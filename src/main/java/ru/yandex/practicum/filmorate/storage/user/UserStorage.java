package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    //create
    User addUser(User user);
    //read
    User getUser(int id);
    Map<Integer, User> getUsers();
    //update
    User updateUser(User user);
    //delete
    void clearUserStorage();
}
