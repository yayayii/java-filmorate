package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Map;

public interface MpaStorage {
    Mpa getMpa(int id);

    Map<Integer, Mpa> getMpas();
}
