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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageApplicationTest {
    private final UserDbStorage userStorage;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
    }

    @AfterEach
    void afterEach() {
        userStorage.clearUserStorage();
    }

    @Test
    public void testAddAndGetUser() {
        userStorage.addUser(user);
        assertThat(userStorage.getUser(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testGetUsers() {
        userStorage.addUser(user);
        userStorage.addUser(user);
        assertThat(userStorage.getUsers()).containsKey(1);
        assertThat(userStorage.getUsers()).containsKey(2);
    }

    @Test
    public void testUpdateUser() {
        userStorage.addUser(user);
        assertThat(userStorage.getUser(1)).hasFieldOrPropertyWithValue("login", "login");

        user.setId(1);
        user.setLogin("new login");
        userStorage.updateUser(user);
        assertThat(userStorage.getUser(1)).hasFieldOrPropertyWithValue("login", "new login");
    }

    @Test
    public void testClearUserStorage() {
        userStorage.addUser(user);
        assertThat(userStorage.getUsers()).hasSize(1);

        userStorage.clearUserStorage();
        assertThat(userStorage.getUsers()).hasSize(0);
    }
}
