package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.like.LikeStorage;

import java.util.Map;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    //inMemoryFilmStorage / filmDbStorage
    //inMemoryLikeStorage / likeDbStorage
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("likeDbStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    //films
    //create
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }
    //read
    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }
    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }
    //update
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
    //delete
    public void clearFilmStorage() {
        filmStorage.clearFilmStorage();
    }

    //likes
    //create
    public void addLike(int filmId, int userId) {
        likeStorage.addLike(filmId, userId);
    }
    //read
    public Set<Integer> getLikedUsersIds(int filmId) {
        return likeStorage.getLikedUsersIds(filmId);
    }
    public Set<Film> getPopularFilms(int count) {
        return likeStorage.getPopularFilms(count);
    }
    //update
    //delete
    public void removeLike(int filmId, int userId) {
        likeStorage.removeLike(filmId, userId);
    }
}
