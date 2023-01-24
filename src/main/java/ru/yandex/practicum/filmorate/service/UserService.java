package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

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
        userStorage.getUser(userId).getFriendsIds().add(friendId);
        userStorage.getUser(friendId).getFriendsIds().add(userId);
    }
    //read
    public Set<User> getFriends(int userId) {
        Set<User> friends = userStorage.getUser(userId).getFriendsIds()
                .stream().map(userStorage::getUser).collect(Collectors.toSet());
        Set<User> sortedFriends = new TreeSet<>(Comparator.comparingInt(User::getId));
        sortedFriends.addAll(friends);
        return sortedFriends;
    }

    public Set<User> getCommonFriends(int userId, int anotherUserId) {
        Set<Integer> mutualFriends = new HashSet<>(userStorage.getUser(userId).getFriendsIds());
        mutualFriends.retainAll(userStorage.getUser(anotherUserId).getFriendsIds());
        Set<User> sortedMutualFriends = new TreeSet<>(Comparator.comparingInt(User::getId));
        sortedMutualFriends.addAll(mutualFriends.stream().map(userStorage::getUser).collect(Collectors.toSet()));
        return sortedMutualFriends;
    }
    //update
    //delete
    public void deleteFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriendsIds().remove(friendId);
        userStorage.getUser(friendId).getFriendsIds().remove(userId);
    }






}
