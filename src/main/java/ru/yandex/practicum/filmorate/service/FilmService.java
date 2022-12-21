package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
