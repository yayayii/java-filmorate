package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.film.MpaInMemory;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryMpaStorage implements MpaStorage{
    @Override
    public Mpa getMpa(int id) {
        MpaInMemory mpa = MpaInMemory.forValues(id);
        if (mpa == null) {
            return null;
        }
        return new Mpa(mpa.getId(), mpa.getName());
    }

    @Override
    public Map<Integer, Mpa> getMpas() {
        Map<Integer, Mpa> mpas = new HashMap<>();
        for (MpaInMemory mpa : MpaInMemory.values()) {
            mpas.put(mpa.getId(), new Mpa(mpa.getId(), mpa.getName()));
        }
        return mpas;
    }
}
