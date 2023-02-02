package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.film.Mpa;

public interface MpaStorage {
    Mpa getMpa(int id);
    Mpa[] getMpas();
}
