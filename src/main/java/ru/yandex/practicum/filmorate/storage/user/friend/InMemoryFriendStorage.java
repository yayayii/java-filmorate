package ru.yandex.practicum.filmorate.storage.user.friend;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final InMemoryUserStorage userStorage;

    public InMemoryFriendStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //create
    @Override
    public void addFriend(int userId, int friendId) {
        userStorage.getUser(userId).getFriendsIds().add(friendId);

        if (userStorage.getUser(userId).getFriendsIds().contains(friendId) &&
                userStorage.getUser(friendId).getFriendsIds().contains(userId)) {
            userStorage.getUser(userId).getConfirmedFriendsIds().add(friendId);
            userStorage.getUser(friendId).getConfirmedFriendsIds().add(userId);
        }
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
    public Set<User> getConfirmedFriends(int userId) {
        Set<User> friends = userStorage.getUser(userId).getConfirmedFriendsIds()
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

        userStorage.getUser(userId).getConfirmedFriendsIds().remove(friendId);
        userStorage.getUser(friendId).getConfirmedFriendsIds().remove(userId);
    }
}
