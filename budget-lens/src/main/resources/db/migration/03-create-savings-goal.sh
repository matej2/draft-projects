#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL


    -- TABLES

    CREATE TABLE IF NOT EXISTS savingsgoal (
      id serial primary key,
      name varchar(100),
      target_amount numeric,
      current_amount numeric,
      deadline date
    );

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON savingsgoal TO $APP_USERNAME;


EOSQL