package ru.yandex.practicum.filmorate.storage.film.like;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class LikeDbStorage implements LikeStorage{
    private final JdbcTemplate jdbcTemplate;

    //create
    @Override
    public void addLike(int filmId, int userId) {
        String sql = "insert into liked_film " +
                "values " +
                "(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);

        sql = "update film " +
                "set likes_count = likes_count+1 " +
                "where id = ?";
        jdbcTemplate.update(sql, filmId);
    }
    //read
    @Override
    public Set<Integer> getLikedUsersIds(int filmId) {
        String sql = "select user_id " +
                "from liked_film " +
                "where film_id = ?";

        return new HashSet<>(
                jdbcTemplate.queryForList(sql, Integer.class, filmId)
        );
    }

    @Override
    public Set<Film> getPopularFilms(int count) {
        String sql = "select f.*, group_concat(fg.genre_id) as genre_ids " +
                "from film as f " +
                "left join film_genre as fg " +
                "on f.id = fg.film_id " +
                "group by f.id " +
                "order by f.likes_count desc, f.id " +
                "limit ?";

        return new HashSet<>(jdbcTemplate.query(
                sql,
                new Object[]{count},
                FilmDbStorage::mapRowToFilm)
        );
    }
    //update
    //delete
    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "delete from liked_film " +
                "where film_id = ? " +
                "and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);

        sql = "update film " +
                "set likes_count = likes_count-1 " +
                "where id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
