package ru.yandex.practicum.filmorate.storage.film.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Map;

public interface GenreStorage {
    Genre getGenre(int id);
    Map<Integer, Genre> getGenres();
}
