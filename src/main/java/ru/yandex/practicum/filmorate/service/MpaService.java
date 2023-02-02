package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import java.util.Map;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    //inMemoryMpaStorage / mpaDbStorage
    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpa(int id) {
        return mpaStorage.getMpa(id);
    }
    public Map<Integer, Mpa> getMpas() {
        return mpaStorage.getMpas();
    }
}
