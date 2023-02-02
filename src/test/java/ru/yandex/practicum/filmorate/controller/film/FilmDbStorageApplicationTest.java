package ru.yandex.practicum.filmorate.controller.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.GenreInMemory;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageApplicationTest {
    private final FilmDbStorage filmStorage;

    private Film film;

    @BeforeEach
    void beforeEach() {
        film = new Film("name", "Description", LocalDate.of(2000, 1, 1), 120, new Mpa(1, "G"), Set.of(new Genre(1, "Комедия")));
    }

    @AfterEach
    void afterEach() {
        filmStorage.clearFilmStorage();
    }

    @Test
    public void testAddAndGetFilm() {
        filmStorage.addFilm(film);
        assertThat(filmStorage.getFilm(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testGetFilms() {
        filmStorage.addFilm(film);
        filmStorage.addFilm(film);
        assertThat(filmStorage.getFilms()).containsKey(1);
        assertThat(filmStorage.getFilms()).containsKey(2);
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.addFilm(film);
        assertThat(filmStorage.getFilm(1)).hasFieldOrPropertyWithValue("name", "name");

        film.setId(1);
        film.setName("new name");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilm(1)).hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    public void testClearFilmStorage() {
        filmStorage.addFilm(film);
        assertThat(filmStorage.getFilms()).hasSize(1);

        filmStorage.clearFilmStorage();
        assertThat(filmStorage.getFilms()).hasSize(0);
    }
}
