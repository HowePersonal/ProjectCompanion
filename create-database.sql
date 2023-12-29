CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    email varchar(100) NOT NULL,
    username varchar(50) UNIQUE NOT NULL,
    password varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS userchat (
    id serial PRIMARY KEY,
    user_id INT NOT NULL,
    input varchar(300) NOT NULL,
    response varchar(300) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
