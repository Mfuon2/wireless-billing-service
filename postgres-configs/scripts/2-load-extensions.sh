#!/bin/bash

set -e

# You could probably do this fancier and have an array of extensions
# to create, but this is mostly an illustration of what can be done

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB"  <<EOF

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

select * FROM pg_extension;
EOF