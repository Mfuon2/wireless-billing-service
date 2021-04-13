#!/bin/bash
# set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL

    CREATE USER vuka_user;
    CREATE SCHEMA IF NOT EXISTS vuka_wireless AUTHORIZATION vuka_user;
    GRANT ALL PRIVILEGES ON SCHEMA vuka_wireless TO vuka_user;

    SELECT schema_name FROM information_schema.schemata;

EOSQL