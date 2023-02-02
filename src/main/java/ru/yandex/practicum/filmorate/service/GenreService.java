package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;

@Service
public class GenreService {
    public Genre getGenre(int id) {
        return Genre.forValues(id);
    }
    public Genre[] getGenres() {
        return Genre.values();
    }
}
