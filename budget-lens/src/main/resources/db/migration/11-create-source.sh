#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
CREATE TABLE IF NOT EXISTS source (
        id serial primary key,
        source varchar(100),
        category integer references category(id)
    );

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON source TO $APP_USERNAME;

    GRANT USAGE, SELECT ON SEQUENCE source_id_seq TO $APP_USERNAME;
EOSQL