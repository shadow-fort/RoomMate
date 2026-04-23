DROP TABLE if EXISTS room;
DROP TABLE if EXISTS workplace;
DROP TABLE if EXISTS person;
DROP TABLE if EXISTS booking;

CREATE TABLE room
(
    room_id   serial primary key,
    room_name varchar(50) unique not null
);

CREATE TABLE workplace
(
    room   int references room (room_id),
    workplace_nummer      int,
    equipments text,
    status varchar(10)
);

CREATE TABLE person
(
    id       serial primary key,
    title    varchar(20),
    username varchar(50) unique not null,
    vorname  varchar(50),
    nachname varchar(50)
);

CREATE TABLE booking
(
    id       serial primary key,
    version       integer,
    username         varchar(50),
    room_name varchar(50),
    workplace_number int
);

CREATE TABLE period
(
    booking   int references booking (id),
    date             date,
    start_time       time,
    end_time         time
)