#!/bin/bash
set -e
PGPASSWORD=${SEED_PASS}

psql -v ON_ERROR_STOP=1 --username "seed_user" --dbname "$POSTGRES_DB" <<-EOSQL
  INSERT INTO frequency VALUES
    (1, "Yearly"),
    (12, "Monthly"),
    (52, "Weekly"),
    (365, "Daily");
EOSQL