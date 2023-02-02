package ru.yandex.practicum.filmorate.controller.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.friend.FriendDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageApplicationTest {
    private final FriendDbStorage friendStorage;
    private final UserDbStorage userStorage;

    @AfterEach
    void afterEach() {
        userStorage.clearUserStorage();
    }

    @Test
    public void testAddAndGetFriends() {
        User user1 = new User("email1@qwe.ru", "login1", "name", LocalDate.of(2000, 1, 1));
        User user2 = new User("email2@qwe.ru", "login2", "name", LocalDate.of(2000, 1, 1));
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        user1.setId(1);
        user2.setId(2);

        friendStorage.addFriend(1, 2);
        assertThat(friendStorage.getFriends(1)).contains(user2);
        assertThat(friendStorage.getFriends(2)).doesNotContain(user1);
    }

    @Test
    public void testGetCommonFriends() {
        User user1 = new User("email1@qwe.ru", "login1", "name", LocalDate.of(2000, 1, 1));
        User user2 = new User("email2@qwe.ru", "login2", "name", LocalDate.of(2000, 1, 1));
        User user3 = new User("email3@qwe.ru", "login3", "name", LocalDate.of(2000, 1, 1));
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);

        friendStorage.addFriend(1, 3);
        friendStorage.addFriend(2, 3);
        assertThat(friendStorage.getCommonFriends(1, 2)).contains(user3);
        assertThat(friendStorage.getCommonFriends(1, 3)).hasSize(0);
    }

    @Test
    public void testDeleteFriend() {
        User user1 = new User("email1@qwe.ru", "login1", "name", LocalDate.of(2000, 1, 1));
        User user2 = new User("email2@qwe.ru", "login2", "name", LocalDate.of(2000, 1, 1));
        userStorage.addUser(user1);
        userStorage.addUser(user2);

        friendStorage.addFriend(1, 2);
        assertThat(friendStorage.getFriends(1)).hasSize(1);

        friendStorage.deleteFriend(1, 2);
        assertThat(friendStorage.getFriends(1)).hasSize(0);
    }
}
