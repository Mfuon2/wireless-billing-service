#!/bin/bash
# set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB"  <<EOF

    CREATE EXTENSION IF NOT EXISTS "pgcrypto";
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

    SELECT * FROM pg_extension;

EOF


# # Declare an array of string with type
# declare -a StringArray=("$POSTGRES_DB" "vuka_wireless" "my_biz" )
 
#  ALTER EXTENSION "uuid-ossp" SET SCHEMA public;

# # Iterate the string array using for loop
# for val in ${StringArray[@]}; do
#    echo $val
# done
