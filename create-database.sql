CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    email varchar(100) NOT NULL,
    username varchar(50) UNIQUE NOT NULL,
    password varchar(100) NOT NULL
);