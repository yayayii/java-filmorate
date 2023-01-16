# java-filmorate
Template repository for Filmorate project.

![Database diagram](/Untitled.png)

selecting all films:

    SELECT *
    FROM film;

selecting all users:

	SELECT *
	FROM user;

selecting top X most popular films:

	SELECT name, COUNT(*)
	FROM liked_film as lf
	JOIN film as f
	ON f.id = lf.film_id
	GROUP BY f.id
	ORDER BY COUNT(*) DESC
	LIMIT X;

selecting common friends with other user:

	SELECT u.login AS common_friend
	FROM (
		SELECT f1.user_id
		FROM friend AS f1
		JOIN friend AS f2
		ON f1.user_id = f2.other_user_id
		AND f2.user_id = f1.other_user_id
		AND f1.user_id < f1.other_user_id
		WHERE f1.other_user_id = 1
		UNION
		SELECT f1.other_user_id
		FROM friend AS f1
		JOIN friend AS f2
		ON f1.user_id = f2.other_user_id
		AND f2.user_id = f1.other_user_id
		AND f1.user_id < f1.other_user_id
		WHERE f1.user_id = 1
	) AS user_friend
	JOIN (
		SELECT f1.user_id
		FROM friend as f1
		JOIN friend as f2
		ON f1.user_id = f2.other_user_id
		AND f2.user_id = f1.other_user_id
		AND f1.user_id < f1.other_user_id
		WHERE f1.other_user_id = 4
		UNION
		SELECT f1.other_user_id
		FROM friend AS f1
		JOIN friend AS f2
		ON f1.user_id = f2.other_user_id
		AND f2.user_id = f1.other_user_id
		AND f1.user_id < f1.other_user_id
		WHERE f1.user_id = 4
	) AS other_user_friend
	ON user_friend.user_id = other_user_friend.user_id
	JOIN "user" AS u
	ON u.id = user_friend.user_Id;