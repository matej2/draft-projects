#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL


    -- TABLES

    CREATE TABLE IF NOT EXISTS budget (
      id serial primary key,
      note varchar(200),
      quota real,
      category integer references category(id),
      frequency integer references frequency(id)
    );

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON budget TO $APP_USERNAME;

    GRANT USAGE, SELECT ON SEQUENCE budget_id_seq TO $APP_USERNAME;

EOSQL