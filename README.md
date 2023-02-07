# java-filmorate
Template repository for Filmorate project.

![Database diagram](/Untitled.png)

selecting all films:

    SELECT f.*, GROUP_CONCAT(fg.genre_id) AS genre_ids
    FROM film AS f
    LEFT JOIN film_genre AS fg
    ON f.id = fg.film_id
    GROUP BY f.id;

selecting all users:

	SELECT *
	FROM user;

selecting top X most popular films:

	SELECT f.*, GROUP_CONCAT(fg.genre_id) AS genre_ids, GROUP_CONCAT(g.name) AS genre_names
	FROM film AS f
    JOIN mpa AS m
    on f.mpa_id = m.id
	LEFT JOIN film_genre AS fg
	ON f.id = fg.film_id
    LEFT JOIN genre AS g
    ON fg.genre_id = g.id
    GROUP BY f.id
    ORDER BY f.likes_count DESC, f.id
    LIMIT ?;

selecting common friends with other user:

	SELECT u.*
	FROM (
		SELECT f1.user_id
		FROM friend AS f1
		WHERE f1.other_user_id = ?
		UNION
		SELECT f1.other_user_id
		FROM friend AS f1
		WHERE f1.user_id = ?
	) AS user_friend
	JOIN (
		SELECT f1.user_id
		FROM friend AS f1
		WHERE f1.other_user_id = ?
		UNION
		SELECT f1.other_user_id
		FROM friend AS f1
		WHERE f1.user_id = ?
	) AS other_user_friend
	ON user_friend.user_id = other_user_friend.user_id
	JOIN user AS u
	ON u.id = user_friend.user_id;
