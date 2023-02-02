package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Set;

public interface MpaStorage {
    Mpa getMpa(int id);
    Set<Mpa> getMpas();
}
