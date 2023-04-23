package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenreInMemory {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    ANIMATION(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");


    private final int id;
    private final String name;


    @JsonCreator
    public static GenreInMemory forValues(int id) {
        for (GenreInMemory genre : GenreInMemory.values()) {
            if (id == genre.id) {
                return genre;
            }
        }
        return null;
    }
}
