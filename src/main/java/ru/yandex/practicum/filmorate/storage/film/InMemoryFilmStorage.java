package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
        log.info("Film \"" + film.getName() + "\" was added.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Film \"" + film.getName() + "\" was updated.");
        return film;
    }

    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public void clearFilmStorage() {
        id = 0;
        films.clear();
        log.info("Film storage was cleared.");
    }
}
