#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL


    -- TABLES

    CREATE TABLE IF NOT EXISTS budget (
      id serial primary key,
      monthly_limit real,
      category integer references category(id)
    );

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON budget TO app_user;


EOSQL