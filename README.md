MONO REPO

Projects
<!-- 1. USSD flow

```
    cd ussd-flow
    mvn clean package
```

2. USSD rest api 

```
    cd ussd-api
    mvn clean package
``` -->

# Running in local dev

```
#. Fastest / Useless
docker-compose build && docker-compose up

# Rebuild everytime
docker-compose build && docker-compose up --force-recreate

# Build everything from scratch, without using cache, ouch !
docker-compose build --no-cache && docker-compose up --force-recreate
```

# Running in uat / smart 
file: docker-compose-covid-ussd-uat.yml