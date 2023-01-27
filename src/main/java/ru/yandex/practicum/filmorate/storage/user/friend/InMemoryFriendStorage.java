package ru.yandex.practicum.filmorate.storage.user.friend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final UserStorage userStorage;

    public InMemoryFriendStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //create
    @Override
    public void addFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriendsIds().add(friendId);
    }
    //read
    @Override
    public Set<User> getFriends(int userId) {
        Set<User> friends = userStorage.getUser(userId).getFriendsIds()
                .stream().map(userStorage::getUser).collect(Collectors.toSet());
        Set<User> sortedFriends = new TreeSet<>(Comparator.comparingInt(User::getId));
        sortedFriends.addAll(friends);
        return sortedFriends;
    }

    @Override
    public Set<User> getCommonFriends(int userId, int anotherUserId) {
        Set<Integer> mutualFriends = new HashSet<>(userStorage.getUser(userId).getFriendsIds());
        mutualFriends.retainAll(userStorage.getUser(anotherUserId).getFriendsIds());
        Set<User> sortedMutualFriends = new TreeSet<>(Comparator.comparingInt(User::getId));
        sortedMutualFriends.addAll(mutualFriends.stream().map(userStorage::getUser).collect(Collectors.toSet()));
        return sortedMutualFriends;
    }
    //update
    //delete
    @Override
    public void deleteFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriendsIds().remove(friendId);
        userStorage.getUser(friendId).getFriendsIds().remove(userId);
    }
}
