package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private transient final int id;
    private final String name;
    private transient final String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private transient final LocalDate releaseDate;
    private transient final int duration;
}
