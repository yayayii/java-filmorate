package ru.yandex.practicum.filmorate.storage.film.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.GenreInMemory;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryGenreStorage implements GenreStorage{
    @Override
    public Genre getGenre(int id) {
        GenreInMemory genre = GenreInMemory.forValues(id);
        if (genre == null) {
            return null;
        }
        return new Genre(genre.getId(), genre.getName());
    }

    @Override
    public Map<Integer, Genre> getGenres() {
        Map<Integer, Genre> genres = new HashMap<>();
        for (GenreInMemory genre : GenreInMemory.values()) {
            genres.put(genre.getId(), new Genre(genre.getId(), genre.getName()));
        }
        return genres;
    }
}
