package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

import java.util.Map;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    //inMemoryGenreStorage / genreDbStorage
    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenre(int id) {
        return genreStorage.getGenre(id);
    }
    public Map<Integer, Genre> getGenres() {
        return genreStorage.getGenres();
    }
}
