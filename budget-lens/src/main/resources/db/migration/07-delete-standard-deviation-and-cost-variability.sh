#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  ALTER TABLE insight
  DROP standard_deviation bigint;

  ALTER TABLE insight
  DROP is_cost_variable date;

EOSQL