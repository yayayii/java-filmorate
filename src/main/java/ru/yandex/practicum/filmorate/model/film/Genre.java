package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;

@Data
public class Genre implements Comparable<Genre> {
    private final int id;
    private final String name;

    @Override
    public int compareTo(Genre o) {
        return Integer.compare(id, o.id);
    }
}
