package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

import java.util.Set;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenre(int id) {
        return genreStorage.getGenre(id);
    }
    public Set<Genre> getGenres() {
        return genreStorage.getGenres();
    }
}
