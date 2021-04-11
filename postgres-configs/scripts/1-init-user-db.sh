#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER vuka_user;
    CREATE DATABASE vuka_wireless;
    GRANT ALL PRIVILEGES ON DATABASE vuka_wireless TO vuka_user;
EOSQL