package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;

@Data
public class Mpa implements Comparable<Mpa> {
    private final int id;
    private final String name;

    @Override
    public int compareTo(Mpa o) {
        return Integer.compare(id, o.id);
    }
}
