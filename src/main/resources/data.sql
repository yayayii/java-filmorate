delete from film_genre;
delete from friend;
delete from genre;
delete from liked_film;
delete from film;
delete from mpa;
delete from users;

alter table genre
    alter column id
        restart with 1;

insert into genre (name)
values ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

alter table mpa
    alter column id
        restart with 1;

insert into mpa (name)
values ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

alter table film
    alter column id
        restart with 1;

alter table users
    alter column id
        restart with 1;