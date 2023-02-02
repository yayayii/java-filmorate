package ru.yandex.practicum.filmorate.storage.film.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.GenreInMemory;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

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
    public Set<Genre> getGenres() {
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        for (GenreInMemory genre : GenreInMemory.values()) {
            genres.add(new Genre(genre.getId(), genre.getName()));
        }
        return genres;
    }
}
