package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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

    public Set<Film> getPopularFilms(int count) {
        Set<Film> popularFilms = new TreeSet<>((o1, o2) -> {
            if (o1.getLikedUsersIds().size() > o2.getLikedUsersIds().size()) {
                return -1;
            } else {
                return 1;
            }
        });
        popularFilms.addAll(filmStorage.getFilms().values());
        return popularFilms.stream().limit(count).collect(Collectors.toSet());
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
        filmStorage.getFilm(filmId).getLikedUsersIds().add(userId);
    }
    //read
    //update
    //delete
    public void removeLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().remove(userId);
    }

    //mpa
    //create
    //read
    public Mpa getMpa(int id) {
        return Mpa.forValues(id);
    }

    public Mpa[] getMpas() {
        return Mpa.values();
    }
    //update
    //delete

    //genre
    //create
    //read
    public Genre getGenre(int id) {
        return Genre.forValues(id);
    }

    public Genre[] getGenres() {
        return Genre.values();
    }
    //update
    //delete
}
