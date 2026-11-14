CREATE TABLE stations (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          total_tracks INT NOT NULL CHECK (total_tracks > 0)
);

CREATE TABLE trains (
                        id SERIAL PRIMARY KEY,
                        train_code VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE train_stops (
                             id SERIAL PRIMARY KEY,
                             train_id INT NOT NULL REFERENCES trains(id) ON DELETE CASCADE,
                             station_id INT NOT NULL REFERENCES stations(id) ON DELETE CASCADE,
                             stop_order INT NOT NULL,
                             planned_arrival TIME NULL,
                             planned_departure TIME NULL,
                             accumulated_delay INT NOT NULL DEFAULT 0,
                             actual_arrival TIME NULL,
                             actual_departure TIME NULL,
                             UNIQUE (train_id, stop_order)
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'OPERATOR'))
);

CREATE TABLE conflicts (
                           id SERIAL PRIMARY KEY,
                           station_id INT NOT NULL REFERENCES stations(id) ON DELETE CASCADE,
                           train_id INT NOT NULL REFERENCES trains(id) ON DELETE CASCADE,
                           user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                           conflict_time TIME NOT NULL,
                           extra_delay INT NOT NULL
);