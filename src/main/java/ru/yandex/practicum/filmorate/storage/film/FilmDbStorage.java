package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.mapper.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;


    //create
    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into film " +
                "(name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String sqlGenres = "insert into film_genre " +
                    "values (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenres, film.getId(), genre.getId());
            }
        }

        log.info("Film \"" + film.getName() + "\" was added.");
        return film;
    }

    //read
    @Override
    public Film getFilm(int id) {
        String sql = "select f.*, m.name as mpa_name, group_concat(fg.genre_id) as genre_ids, group_concat(g.name) as genre_names " +
                "from film as f " +
                "join mpa as m " +
                "on f.mpa_id = m.id " +
                "left join film_genre as fg " +
                "on f.id = fg.film_id " +
                "left join genre as g " +
                "on fg.genre_id = g.id " +
                "where f.id = ? " +
                "group by f.id";

        return jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        String sql = "select f.*, m.name as mpa_name, group_concat(fg.genre_id) as genre_ids, group_concat(g.name) as genre_names " +
        "from film as f " +
        "join mpa as m " +
        "on f.mpa_id = m.id " +
        "left join film_genre as fg " +
        "on f.id = fg.film_id " +
        "left join genre as g " +
        "on fg.genre_id = g.id " +
        "group by f.id";

        return jdbcTemplate.query(sql, new FilmMapper()).
                stream().collect(Collectors.toMap(Film::getId, Function.identity()));
    }

    //update
    @Override
    public Film updateFilm(Film film) {
        String sql = "update film " +
                "set name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "where id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        if (film.getGenres() != null) {
            sql = "delete from film_genre " +
                    "where film_id = ?";
            jdbcTemplate.update(sql, film.getId());

            if (!film.getGenres().isEmpty()) {
                sql = "insert into film_genre " +
                        "values (?, ?)";
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(sql, film.getId(), genre.getId());
                }
            }
        }

        log.info("Film \"" + film.getName() + "\" was updated.");
        return film;
    }

    //delete
    @Override
    public void clearFilmStorage() {
        String sql = "delete from film";
        jdbcTemplate.update(sql);

        sql = "alter table film " +
                "alter column id " +
                "restart with 1";
        jdbcTemplate.update(sql);

        log.info("Film storage was cleared.");
    }
}
