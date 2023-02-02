package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class UserDbStorage implements UserStorage{
    private JdbcTemplate jdbcTemplate;

    //create
    @Override
    public User addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into users " +
                "(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());

        log.info("User \"" + user.getLogin() + "\" was added.");
        return user;
    }
    //read
    @Override
    public User getUser(int id) {
        String sql = "select * " +
                "from users " +
                "where id = ?";

        return jdbcTemplate.queryForObject(sql, UserDbStorage::mapRowToUser, id);
    }
    @Override
    public Map<Integer, User> getUsers() {
        String sql = "select * " +
                "from users";

        return jdbcTemplate.query(sql, UserDbStorage::mapRowToUser).
                stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
    //update
    @Override
    public User updateUser(User user) {
        String sql = "update users " +
                "set email = ?, " +
                "login = ?, " +
                "name = ?, " +
                "birthday = ? " +
                "where id = ?";

        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());

        log.info("User \"" + user.getLogin() + "\" was updated.");
        return user;
    }
    //delete
    @Override
    public void clearUserStorage() {
        String sql = "delete from users";
        jdbcTemplate.update(sql);

        sql = "alter table users " +
                "alter column id " +
                "restart with 1";
        jdbcTemplate.update(sql);

        log.info("User storage was cleared.");
    }

    public static User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }
}
