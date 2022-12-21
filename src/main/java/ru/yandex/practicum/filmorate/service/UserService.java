package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriendsIds().add(friendId);
        userStorage.getUser(friendId).getFriendsIds().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriendsIds().remove(friendId);
        userStorage.getUser(friendId).getFriendsIds().remove(userId);
    }

    public Set<User> getFriends(int userId) {
        return userStorage.getUser(userId).getFriendsIds()
                .stream().map(id -> userStorage.getUser(id)).collect(Collectors.toSet());
    }

    public Set<User> getMutualFriends(int userId, int anotherUserId) {
        Set<Integer> mutualFriendsIds = new HashSet<>(userStorage.getUser(userId).getFriendsIds());
        mutualFriendsIds.retainAll(userStorage.getUser(anotherUserId).getFriendsIds());
        return mutualFriendsIds.stream().map(id -> userStorage.getUser(id)).collect(Collectors.toSet());
    }
}
