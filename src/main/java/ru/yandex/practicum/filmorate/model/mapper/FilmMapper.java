package ru.yandex.practicum.filmorate.model.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = mpaStorage.getMpa(rs.getInt("mpa_id"));
        Set<Genre> genres;
        if (rs.getString("genre_ids") != null) {
            genres = getGenres(rs.getString("genre_ids"));
        } else {
            genres = Collections.emptySet();
        }

        return new Film(id, name, description, releaseDate, duration, mpa, genres);
    }

    private Set<Genre> getGenres(String genreIds) {
        Set<Genre> genres = new HashSet<>();
        for (String genreId : genreIds.split(",")) {
            genres.add(genreStorage.getGenre(Integer.parseInt(genreId)));
        }
        return genres;
    }
}
