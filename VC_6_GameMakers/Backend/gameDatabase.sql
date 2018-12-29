/*
	Tables for game.
    Paige Ahlrichs
*/
-- DROP DATABASE IF EXISTS gameDatabase;
-- CREATE DATABASE gameDatabase;

USE gameDatabase;

CREATE TABLE players (
playerID smallint(3),
username varchar(10),
password char(8),
primary key(playerID)
);

CREATE TABLE stats (
playerID smallint(3),
wins int(3),
loses int(3),
primary key(playerID),
foreign key(playerId) references players(playerID)
);