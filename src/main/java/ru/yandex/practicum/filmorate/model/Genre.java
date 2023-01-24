package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genre {
    COMEDY(1, "Comedy"),
    DRAMA(2, "Drama"),
    ANIMATION(3, "Animation"),
    THRILLER(4, "Thriller"),
    DOCUMENTARY(5, "Documentary"),
    ACTION(6, "Action");

    private final int id;
    private final String name;

    @JsonCreator
    public static Genre forValues(int id) {
        for (Genre genre : Genre.values()) {
            if (id == genre.id) {
                return genre;
            }
        }
        return null;
    }
}
