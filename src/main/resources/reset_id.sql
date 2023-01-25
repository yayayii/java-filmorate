alter table film
    alter column id
        restart with 1;

alter table users
    alter column id
        restart with 1;

alter table genre
    alter column id
        restart with 1;

alter table mpa
    alter column id
        restart with 1;