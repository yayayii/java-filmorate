package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class User {
    private int id;
    private transient String email;
    private transient String login;
    private transient String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private transient LocalDate birthday;

    public User() {
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
