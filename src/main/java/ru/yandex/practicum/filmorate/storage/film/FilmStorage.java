package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {
    Set<Film> getFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
    void clearFilmCollection();
}
