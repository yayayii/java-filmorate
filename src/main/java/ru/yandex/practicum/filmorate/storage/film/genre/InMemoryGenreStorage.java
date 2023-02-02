package ru.yandex.practicum.filmorate.storage.film.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;

@Component
public class InMemoryGenreStorage implements GenreStorage{
    @Override
    public Genre getGenre(int id) {
        return Genre.forValues(id);
    }

    @Override
    public Genre[] getGenres() {
        return Genre.values();
    }
}
