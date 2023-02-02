package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Mpa;

@Component
public class InMemoryMpaStorage implements MpaStorage{
    @Override
    public Mpa getMpa(int id) {
        return Mpa.forValues(id);
    }

    @Override
    public Mpa[] getMpas() {
        return Mpa.values();
    }
}
