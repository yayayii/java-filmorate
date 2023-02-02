package ru.yandex.practicum.filmorate.storage.film.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Set;

public interface GenreStorage {
    Genre getGenre(int id);
    Set<Genre> getGenres();
}
