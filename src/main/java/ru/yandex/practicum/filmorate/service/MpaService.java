package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Mpa;

@Service
public class MpaService {
    public Mpa getMpa(int id) {
        return Mpa.forValues(id);
    }
    public Mpa[] getMpas() {
        return Mpa.values();
    }
}
