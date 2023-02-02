package ru.yandex.practicum.filmorate.storage.film.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

public interface GenreStorage {
    Genre getGenre(int id);
    Genre[] getGenres();
}
