package ru.yandex.practicum.filmorate.storage.film.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.mapper.GenreMapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenre(int id) {
        String sql = "select * " +
                "from genre " +
                "where id = ?";

        return jdbcTemplate.queryForObject(sql, new GenreMapper(), id);
    }

    @Override
    public Map<Integer, Genre> getGenres() {
        String sql = "select * " +
                "from genre";

        return jdbcTemplate.query(sql, new GenreMapper()).
                stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
    }
}
