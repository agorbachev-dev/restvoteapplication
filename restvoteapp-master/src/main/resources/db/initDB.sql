DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER   DEFAULT global_seq.nextval PRIMARY KEY,
    name       VARCHAR                 NOT NULL,
    email      VARCHAR                 NOT NULL,
    password   VARCHAR                 NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL,
    enabled    BOOL      DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id   INTEGER DEFAULT global_seq.nextval PRIMARY KEY,
    name VARCHAR NOT NULL,
    CONSTRAINT restaurant_unique_name_idx UNIQUE (name)
);

CREATE TABLE menu
(
    id            INTEGER DEFAULT global_seq.nextval PRIMARY KEY,
    menudate      DATE    DEFAULT current_date NOT NULL,
    restaurant_id INTEGER                      NOT NULL,
    dishes        LONGTEXT                     NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menus_unique_menudate_restaurant_idx UNIQUE (menudate, restaurant_id)
);

CREATE TABLE vote
(
    id            INTEGER DEFAULT global_seq.nextval PRIMARY KEY,
    votedate      DATE    DEFAULT current_date NOT NULL,
    votetime      TIME    DEFAULT current_time NOT NULL,
    restaurant_id INTEGER                      NOT NULL,
    user_id       INTEGER                      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT vote_unique_user_date_idx UNIQUE (user_id, votedate)
);


