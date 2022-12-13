package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Film {
    private int id;
    private transient String name;
    private transient String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private transient LocalDate releaseDate;
    private transient int duration;

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
