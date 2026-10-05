#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  ALTER TABLE insight
  ADD count integer;

  ALTER TABLE insight
  ADD updated_at date;

EOSQL