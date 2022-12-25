package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    //storage
    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void clearFilmStorage() {
        filmStorage.clearFilmStorage();
    }

    //likes
    public void addLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().remove(userId);
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
}
