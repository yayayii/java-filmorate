package ru.yandex.practicum.filmorate.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Mpa(id, name);
    }
}
