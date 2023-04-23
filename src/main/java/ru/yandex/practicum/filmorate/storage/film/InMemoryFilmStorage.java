package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.*;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();


    //create
    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        updateMpa(film);
        updateGenres(film);
        films.put(id, film);
        log.info("Film \"" + film.getName() + "\" was added.");
        return film;
    }

    //read
    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    //update
    @Override
    public Film updateFilm(Film film) {
        updateMpa(film);
        updateGenres(film);
        films.put(film.getId(), film);
        log.info("Film \"" + film.getName() + "\" was updated.");
        return film;
    }

    //delete
    @Override
    public void clearFilmStorage() {
        id = 0;
        films.clear();
        log.info("Film storage was cleared.");
    }


    private void updateMpa(Film film) {
        MpaInMemory mpaInMemory = MpaInMemory.forValues(film.getMpa().getId());
        film.setMpa(new Mpa(mpaInMemory.getId(), mpaInMemory.getName()));
    }

    private void updateGenres(Film film) {
        TreeSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        for (Genre genre : film.getGenres()) {
            GenreInMemory genreInMemory = GenreInMemory.forValues(genre.getId());
            genres.add(new Genre(genreInMemory.getId(), genreInMemory.getName()));
        }
        film.setGenres(genres);
    }
}
