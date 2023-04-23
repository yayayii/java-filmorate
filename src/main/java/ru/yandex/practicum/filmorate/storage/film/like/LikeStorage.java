package ru.yandex.practicum.filmorate.storage.film.like;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Set;

public interface LikeStorage {
    //create
    void addLike(int filmId, int userId);

    //read
    Set<Integer> getLikedUsersIds(int filmId);

    Set<Film> getPopularFilms(int count);

    //update
    //delete
    void removeLike(int filmId, int userId);
}
