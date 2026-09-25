#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL


    -- TABLES
    CREATE TABLE IF NOT EXISTS recordlimit (
      id serial primary key,
      class_name varchar(200),
      current_count bigint,
      record_limit bigint
    );

    INSERT INTO
        recordlimit (class_name, record_limit)
    VALUES
        ('com.matej2.budget_lens.domain.entity.Expense', 10);

    -- Permission grants
    GRANT SELECT, INSERT, UPDATE, DELETE ON recordlimit TO $APP_USERNAME;

    GRANT USAGE, SELECT ON SEQUENCE recordlimit_id_seq TO $APP_USERNAME;


EOSQL