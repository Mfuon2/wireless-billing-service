MONO REPO

# Projects
1. Mpesa gateway

```
    cd mpesa-gateway
    mvn clean package
```

2. Portal rest api

```
    cd portal-api
    mvn clean package
```

# Running in local dev

```
#. Fastest, Requires `mvn clean package`
docker-compose build && docker-compose up

# Rebuild everytime
docker-compose build && docker-compose up --force-recreate

# Build everything from scratch, without using cache, ouch !
docker-compose build --no-cache && docker-compose up --force-recreate
```

# Running in uat / digital ocean / aws
