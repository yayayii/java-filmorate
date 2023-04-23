package ru.yandex.practicum.filmorate.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
        Set<Genre> genres;
        if (rs.getString("genre_ids") != null) {
            genres = getGenres(rs.getString("genre_ids"), rs.getString("genre_names"));
        } else {
            genres = Collections.emptySet();
        }

        return new Film(id, name, description, releaseDate, duration, mpa, genres);
    }

    private Set<Genre> getGenres(String genreIds, String genreNames) {
        Set<Genre> genres = new HashSet<>();
        String[] genreIdsArray = genreIds.split(",");
        String[] genreNamesArray = genreNames.split(",");

        for (int i = 0; i < genreIdsArray.length; i++) {
            genres.add(
                    new Genre(
                            Integer.parseInt(genreIdsArray[i]),
                            genreNamesArray[i]
                    )
            );
        }

        return genres;
    }
}
