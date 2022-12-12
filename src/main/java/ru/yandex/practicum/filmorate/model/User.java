package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private transient final int id;
    private final String email;
    private final String login;
    private transient final String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private transient final LocalDate birthday;
}
