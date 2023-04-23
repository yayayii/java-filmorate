package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MpaInMemory {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");


    private final int id;
    private final String name;


    @JsonCreator
    public static MpaInMemory forValues(int id) {
        for (MpaInMemory mpa : MpaInMemory.values()) {
            if (id == mpa.id) {
                return mpa;
            }
        }
        return null;
    }
}
