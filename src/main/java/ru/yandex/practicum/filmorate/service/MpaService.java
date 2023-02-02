package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getMpa(int id) {
        return mpaStorage.getMpa(id);
    }
    public Mpa[] getMpas() {
        return mpaStorage.getMpas();
    }
}
