#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL


    -- TABLES
    CREATE TABLE IF NOT EXISTS insight (
      id serial primary key,
      year_month date,
      average real,
      standard_deviation real,
      standard_deviation_percent real,
      is_sent_toai boolean,
      category integer references category(id)
    );

    -- CONSTRAINTS
    ALTER TABLE insight
      ADD CONSTRAINT insight_year_month_category_key UNIQUE (year_month, category);

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON insight TO $APP_USERNAME;

    GRANT USAGE, SELECT ON SEQUENCE insight_id_seq TO $APP_USERNAME;


EOSQL