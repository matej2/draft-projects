#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE SCHEMA IF NOT EXISTS read
    CREATE SCHEMA IF NOT EXISTS read_write

    -- app user
    DO \$\$
    BEGIN
       IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'app_user') THEN
          CREATE ROLE app_user LOGIN PASSWORD '${APP_PASS}';
       END IF;
    END
    \$\$;

    -- Seed/admin user
    DO \$\$
    BEGIN
      IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'seed_user') THEN
         CREATE ROLE seed_user LOGIN PASSWORD '${SEED_PASS}';
      END IF;
    END
    \$\$;

    -- Revoke all permissions
    REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHELA public FROM 'app_user'
    REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHELA public FROM 'seed_user'

    -- Grant connect
    GRANT CONNECT ON DATABASE ${POSTGRES_DB} TO app_user;
    GRANT CONNECT ON DATABASE ${POSTGRES_DB} TO seed_user;

    -- Grant usage for sequences
    GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA read TO app_user;
    GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA read_write TO app_user;
    GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO seed_user;

    -- Grant usage on schema
    GRANT USAGE ON SCHEMA read TO app_user;
    GRANT USAGE ON SCHEMA read_write TO app_user;
    GRANT USAGE ON SCHEMA read_write TO seed_user;


    -- Alter default privileges
    ALTER DEFAULT PRIVILEGES FOR ROLE ${POSTGRES_USER} IN SCHEMA read
      GRANT SELECT ON TABLES TO app_user;
    ALTER DEFAULT PRIVILEGES FOR ROLE ${POSTGRES_USER} IN SCHEMA read
          GRANT INSERT ON TABLES TO seed_user;

    -- Remove default user
    DROP USER ${POSTGRES_USER}

EOSQL