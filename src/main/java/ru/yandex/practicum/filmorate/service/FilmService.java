package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService() {
        this.filmStorage = new InMemoryFilmStorage();
    }

    public void addLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().remove(userId);
    }

    public Set<Film> getPopularFilms(int count) {
        return new TreeSet<Film>(Comparator.comparingInt(o -> o.getLikedUsersIds().size()))
                .stream().limit(count).collect(Collectors.toSet());
    }
}
