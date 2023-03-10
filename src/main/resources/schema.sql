DROP TABLE IF EXISTS user_likes CASCADE;
DROP TABLE IF EXISTS user_friends CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS genre;

CREATE TABLE IF NOT EXISTS users
(
    user_id  int generated by default as identity primary key,
    email    varchar(40),
    login    varchar(40),
    name     varchar(40),
    birthday date
);

CREATE TABLE IF NOT EXISTS user_friends
(
    user_friend_id int generated by default as identity primary key,
    user_id        int references users (user_id) ON DELETE CASCADE,
    friend_id      int references users (user_id) ON DELETE CASCADE,
    is_conformed   boolean
);

CREATE TABLE IF NOT EXISTS mpa
(
    id   int generated by default as identity primary key,
    name varchar(15)
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id int generated by default as identity primary key,
    name     varchar(50)
);

CREATE TABLE IF NOT EXISTS film
(
    film_id      INT generated by default as identity PRIMARY KEY,
    name         varchar(50),
    description  varchar(200),
    release_date date,
    duration     int,
    mpa          int references mpa (id) on delete restrict
);

CREATE TABLE IF NOT EXISTS user_likes
(
    user_likes_id int generated by default as identity primary key,
    user_id       int references users (user_id) ON DELETE CASCADE,
    film_id       int references film (film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_genre_id int generated by default as identity primary key,
    film_id       int references film (film_id) on delete cascade,
    genre_id      int references genre (genre_id) on delete cascade
);