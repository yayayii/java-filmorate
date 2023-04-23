package ru.yandex.practicum.filmorate.controller.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.GenreInMemory;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageApplicationTest {
    private final LikeDbStorage likeStorage;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;


    @AfterEach
    void afterEach() {
        userStorage.clearUserStorage();
        filmStorage.clearFilmStorage();
    }


    @Test
    public void testAddLikeAndGetLikedUsersIds() {
        Film film = new Film("name", "Description", LocalDate.of(2000, 1, 1), 120, new Mpa(1, "G"), Set.of(new Genre(1, "Комедия")));
        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        filmStorage.addFilm(film);
        userStorage.addUser(user);
        likeStorage.addLike(1, 1);
        assertThat(likeStorage.getLikedUsersIds(1)).contains(1);
    }

    @Test
    public void testGetPopularFilms() {
        Film film = new Film("name", "Description", LocalDate.of(2000, 1, 1), 120, new Mpa(1, "G"), Set.of(new Genre(1, "Комедия")));
        filmStorage.addFilm(film);
        film.setId(1);
        assertThat(likeStorage.getPopularFilms(1)).contains(film);
    }

    @Test
    public void testRemoveLike() {
        Film film = new Film("name", "Description", LocalDate.of(2000, 1, 1), 120, new Mpa(1, "G"), Set.of(new Genre(1, "Комедия")));
        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        filmStorage.addFilm(film);
        userStorage.addUser(user);
        likeStorage.addLike(1, 1);
        assertThat(likeStorage.getLikedUsersIds(1)).contains(1);

        likeStorage.removeLike(1, 1);
        assertThat(likeStorage.getLikedUsersIds(1)).hasSize(0);
    }
}
