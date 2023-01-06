create table orders
(
    id        serial primary key,
    name      varchar(30) not null,
    surname      varchar(30) not null,
    birthday     varchar(30) not null,
    phone    bigint  not null,
    email  varchar(30) not null,
    list_id  bigint REFERENCES books

);
create table books
(
    id       serial primary key,
    title     varchar(30) not null
);
create table films
(
    id        serial primary key,
    title      varchar(30) not null,
    premier_year      varchar(30) not null,
    country     varchar(30) not null,
    genre    bigint  not null

);

create table users
(
    id        serial primary key,
    login  varchar(30) not null,
    password varchar(30) not null,
    name      varchar(30) not null,
    surname      varchar(30) not null,
    birthDate     varchar(30) not null,
    phone    varchar(30)  not null,
    email  varchar(30) not null,
    createdWhen  varchar(30) not null,
    role_id  bigint REFERENCES books

);