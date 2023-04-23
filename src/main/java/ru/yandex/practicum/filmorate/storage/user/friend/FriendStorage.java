package ru.yandex.practicum.filmorate.storage.user.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendStorage {
    //create
    void addFriend(int userId, int friendId);

    //read
    Set<User> getFriends(int userId);

    Set<User> getConfirmedFriends(int userId);

    Set<User> getCommonFriends(int userId, int anotherUserId);

    //update
    //delete
    void deleteFriend(int userId, int friendId);
}
