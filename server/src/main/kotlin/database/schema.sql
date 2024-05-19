CREATE TABLE IF NOT EXISTS coordinates(
    id SERIAL PRIMARY KEY,
    x INT CHECK(x > -89) NOT NULL,
    y REAL CHECK(y < 483) NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    pass VARCHAR(20) NOT NULL
);

CREATE TYPE fuel_type AS ENUM ('GASOLINE', 'DIESEL', 'MANPOWER', 'ANTIMATTER');

CREATE TYPE vehicle_type AS ENUM ('SHIP', 'CHOPPER', 'HOVERBOARD');

CREATE TABLE IF NOT EXISTS vehicle(
    id BIGSERIAL PRIMARY KEY,
    vehicle_name VARCHAR(255) NOT NULL,
    coordinates_id INT REFERENCES coordinates(id),
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    engine_power DOUBLE_PRECISION CHECK (engine_power > 0) NOT NULL,
    fuel_consumption INT CHECK (fuel_consumption > 0) NOT NULL,
    vehicle_type vehicle_type NOT NULL,
    fuel_type fuel_type NOT NULL,
    created_by INT REFERENCES users(id)
);


