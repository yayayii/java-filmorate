package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friend.FriendStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("friendDbStorage") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    //user
    //create
    public User addUser(User user) {
        return userStorage.addUser(user);
    }
    //read
    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }
    //update
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
    //delete
    public void clearUserStorage() {
        userStorage.clearUserStorage();
    }

    //friends
    //create
    public void addFriend(int userId, int friendId) {
        friendStorage.addFriend(userId, friendId);
    }
    //read
    public Set<User> getFriends(int userId) {
        return friendStorage.getFriends(userId);
    }

    public Set<User> getCommonFriends(int userId, int anotherUserId) {
        return friendStorage.getCommonFriends(userId, anotherUserId);
    }
    //update
    //delete
    public void deleteFriend(int userId, int friendId) {
        friendStorage.deleteFriend(userId, friendId);
    }
}
