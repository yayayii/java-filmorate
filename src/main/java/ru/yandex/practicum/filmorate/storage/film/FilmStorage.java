package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilm(int id);
    void clearFilmStorage();
}
