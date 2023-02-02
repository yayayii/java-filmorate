package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.film.MpaInMemory;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

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
    public Set<Mpa> getMpas() {
        Set<Mpa> mpas = new TreeSet<>(Comparator.comparingInt(Mpa::getId));
        for (MpaInMemory mpa : MpaInMemory.values()) {
            mpas.add(new Mpa(mpa.getId(), mpa.getName()));
        }
        return mpas;
    }
}
