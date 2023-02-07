package ru.yandex.practicum.filmorate.storage.film.like;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class InMemoryLikeStorage implements LikeStorage{
    private final InMemoryFilmStorage filmStorage;

    public InMemoryLikeStorage(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //create
    @Override
    public void addLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().add(userId);
    }
    //read
    @Override
    public Set<Integer> getLikedUsersIds(int filmId) {
        return filmStorage.getFilm(filmId).getLikedUsersIds();
    }
    @Override
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
    //delete
    @Override
    public void removeLike(int filmId, int userId) {
        filmStorage.getFilm(filmId).getLikedUsersIds().remove(userId);
    }
}
