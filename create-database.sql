CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    email varchar(100) NOT NULL,
    username varchar(50) UNIQUE NOT NULL,
    password varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_chat (
    id serial PRIMARY KEY,
    user_id INT NOT NULL,
    input varchar(300) NOT NULL,
    response varchar(300) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS user_coins (
    id serial PRIMARY KEY,
    user_id INT NOT NULL,
    coins INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS user_data (
    id serial PRIMARY KEY,
    user_id INT NOT NULL,
    profile_pic INT NOT NULL,
    description varchar(150),
    tag varchar(20) NOT NULL,
    join_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);