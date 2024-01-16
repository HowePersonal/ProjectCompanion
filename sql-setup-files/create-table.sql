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

CREATE TABLE IF NOT EXISTS user_items (
  id serial PRIMARY KEY,
  user_id INT NOT NULL,
  product_id VARCHAR(50) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS conversations (
    id serial PRIMARY KEY,
    created_timestamp DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS messages (
    id serial PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_content VARCHAR(500) NOT NULL,
    conversation_id INT NOT NULL,
    sent_timestamp DATE NOT NULL,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id),
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);