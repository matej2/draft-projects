#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE TABLE IF NOT EXISTS client (
        id serial primary key,
        username varchar(100),
        password varchar(100),
        role varchar(100)
    );

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON client TO $APP_USERNAME;

    GRANT USAGE, SELECT ON SEQUENCE client_id_seq TO $APP_USERNAME;

    INSERT INTO client (username, password)
    VALUES ('user','\$2a\$12\$ip0YtDAPcYcWaCq6SWFWoeR60hZX6O4Akw47QFGBtHh2nvM1QKm96');
EOSQL