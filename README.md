MONO REPO

# Projects
1. Mpesa gateway

```
    cd mpesa-gateway
    mvn clean package
```

2. Admin portal

```
    cd admin-portal
    yarn install && ng build --delete-output-path --optimization --progress --aot --build-optimizer
```

3. Portal rest api - WIP

```
    cd portal-api
    mvn clean package
```

# Running in local dev

```
# Fastest
    `yarn dockerize`

# Run Step by step
    a. Springboot build: `cd mpesa-gateway && mvn clean package` 
    b. Node build: `cd admin-portal && yarn install && ng build --delete-output-path --optimization --progress --aot --build-optimizer`
    c. Compose startup: `docker-compose build && docker-compose up --force-recreate`

# Build everything from scratch, without using cache, ouch !
docker-compose build --no-cache && docker-compose up --force-recreate
```

# Running in uat local
docker-compose -f docker-compose-uat.yml up -d