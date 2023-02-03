package ru.yandex.practicum.filmorate.storage.user.friend;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.mapper.UserMapper;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class FriendDbStorage implements FriendStorage{
    private final JdbcTemplate jdbcTemplate;

    //create
    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "insert into friend " +
                  "values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);

        sql = "insert into confirmed_friend " +
                "select * " +
                "from friend " +
                "where user_id = ? and other_user_id = ? " +
                "and ( " +
                "    select count(*) " +
                "    from friend " +
                "    where user_id = ? and other_user_id = ? " +
                "       or user_id = ? and other_user_id = ? " +
                ") = 2";
        if (userId < friendId) {
            jdbcTemplate.update(sql, userId, friendId, userId, friendId, friendId, userId);
        } else {
            jdbcTemplate.update(sql, friendId, userId, userId, friendId, friendId, userId);
        }
    }
    //read
    @Override
    public Set<User> getFriends(int userId) {
        String sql = "select u.* " +
                "from friend as f " +
                "join \"USER\" as u " +
                "on f.other_user_id = u.id " +
                "where f.user_id = ?";

        return new HashSet<>(
                jdbcTemplate.query(sql, new Object[]{userId}, new UserMapper())
        );
    }
    @Override
    public Set<User> getConfirmedFriends(int userId) {
        String sql = "select u.* " +
                "from confirmed_friend as f " +
                "join \"USER\" as u " +
                "on f.other_user_id = u.id " +
                "where f.user_id = ? " +
                "union " +
                "select u.* " +
                "from confirmed_friend as f " +
                "join \"USER\" as u " +
                "on f.user_id = u.id " +
                "where f.other_user_id = ? ";

        return new HashSet<>(
                jdbcTemplate.query(sql, new Object[]{userId, userId}, new UserMapper())
        );
    }
    @Override
    public Set<User> getCommonFriends(int userId, int anotherUserId) {
        String sql = "select u.* " +
                "from ( " +
                "   select f1.user_id " +
                "   from friend as f1 " +
                "   where f1.other_user_id = ? " +
                "   union " +
                "   select f1.other_user_id " +
                "   from friend as f1 " +
                "   where f1.user_id = ? " +
                ") as user_friend " +
                "join ( " +
                "   select f1.user_id " +
                "   from friend as f1 " +
                "   where f1.other_user_id = ? " +
                "   union " +
                "   select f1.other_user_id " +
                "   from friend as f1 " +
                "   where f1.user_id = ? " +
                ") as other_user_friend " +
                "on user_friend.user_id = other_user_friend.user_id " +
                "join \"USER\" as u " +
                "on u.id = user_friend.user_id";

        return new HashSet<>(
                jdbcTemplate.query(
                    sql,
                    new Object[]{userId, userId, anotherUserId, anotherUserId},
                    new UserMapper()
                )
        );
    }
    //update
    //delete
    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "delete from friend " +
                    "where user_id = ? " +
                    "and other_user_id = ?";
        jdbcTemplate.update(sql, userId, friendId);

        sql = "delete from confirmed_friend " +
                "where user_id = ? " +
                "and other_user_id = ? " +
                "or user_id = ? " +
                "and other_user_id = ?";
        jdbcTemplate.update(sql, userId, friendId, friendId, userId);
    }
}
