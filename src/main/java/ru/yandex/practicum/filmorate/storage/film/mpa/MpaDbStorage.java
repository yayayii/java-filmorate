package ru.yandex.practicum.filmorate.storage.film.mpa;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.mapper.MpaMapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MpaDbStorage implements MpaStorage{
    private JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpa(int id) {
        String sql = "select * " +
                "from mpa " +
                "where id = ?";

        return jdbcTemplate.queryForObject(sql, new MpaMapper(), id);
    }
    @Override
    public Map<Integer, Mpa> getMpas() {
        String sql = "select * " +
                "from mpa";

        return jdbcTemplate.query(sql, new MpaMapper()).
                stream().collect(Collectors.toMap(Mpa::getId, Function.identity()));
    }
}
