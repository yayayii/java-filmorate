package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    //create
    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        log.info("User \"" + user.getLogin() + "\" was added.");
        return user;
    }
    //read
    @Override
    public User getUser(int id) {
        return users.get(id);
    }
    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }
    //update
    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("User \"" + user.getLogin() + "\" was updated.");
        return user;
    }
    //delete
    @Override
    public void clearUserStorage() {
        id = 0;
        users.clear();
        log.info("User storage was cleared.");
    }
}
