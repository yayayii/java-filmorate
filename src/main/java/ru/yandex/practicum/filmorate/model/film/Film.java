package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Data
public class Film {
    private int id;
    @NotNull @NotBlank @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    private Mpa mpa;
    private TreeSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    @JsonIgnore
    private Set<Integer> likedUsersIds = new HashSet<>();


    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa, Set<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres.addAll(genres);
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres.addAll(genres);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        return id == film.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
