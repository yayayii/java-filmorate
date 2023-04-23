package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Map;

public interface FilmStorage {
    //create
    Film addFilm(Film film);

    //read
    Film getFilm(int id);

    Map<Integer, Film> getFilms();

    //update
    Film updateFilm(Film film);

    //delete
    void clearFilmStorage();
}
