CREATE TABLE IF NOT EXISTS person (
    id SERIAL PRIMARY KEY,
    address VARCHAR(100) NOT NULL,
    first_name VARCHAR(80) NOT NULL,
    gender VARCHAR(6) NOT NULL,
    last_name VARCHAR(80) NOT NULL
    );
