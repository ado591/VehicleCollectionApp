BEGIN;

CREATE SEQUENCE IF NOT EXISTS vehicle_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775806 CACHE 1;

CREATE SEQUENCE IF NOT EXISTS users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;


CREATE TABLE IF NOT EXISTS coordinates(
    id SERIAL PRIMARY KEY,
    x INT NOT NULL CHECK(x > -89),
    y REAL NOT NULL CHECK(y < 483)
);

CREATE TABLE IF NOT EXISTS users(
    id int NOT NULL PRIMARY KEY DEFAULT nextval('users_id_seq'),
    username VARCHAR(50) UNIQUE NOT NULL,
    pass VARCHAR(255) NOT NULL,
    salt VARCHAR(32) NOT NULL
);


CREATE TABLE IF NOT EXISTS vehicle(
    id bigint NOT NULL PRIMARY KEY DEFAULT nextval('vehicle_id_seq'),
    vehicle_name VARCHAR(255) NOT NULL,
    coordinates_id INT REFERENCES coordinates(id) ON DELETE CASCADE,
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    engine_power DOUBLE PRECISION NOT NULL CHECK (engine_power > 0),
    fuel_consumption INT NOT NULL CHECK (fuel_consumption > 0),
    vehicle_type vehicle_type NOT NULL,
    fuel_type fuel_type NOT NULL,
    created_by INT REFERENCES users(id)
);

END;


