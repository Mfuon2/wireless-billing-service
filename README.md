MONO REPO

# Projects
1. Mpesa gateway

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=vuka-mpesa-gateway&metric=alert_status&token=3fcd5e367365192d9a3d956fc665562b6876bb03)](https://sonarcloud.io/dashboard?id=vuka-mpesa-gateway)

```
    cd mpesa-gateway
    mvn clean package
```

2. Admin portal

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=vuka-admin-portal&metric=alert_status&token=3fcd5e367365192d9a3d956fc665562b6876bb03)](https://sonarcloud.io/dashboard?id=vuka-admin-portal)

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

    `cd admin-portal && yarn dockerize`

# Run Step by step
    a. Springboot build: `cd mpesa-gateway && mvn clean package` 
    b. Node build: `cd admin-portal && yarn install && ng build --delete-output-path --optimization --progress --aot --build-optimizer`
    c. Compose startup: `docker-compose build && docker-compose up --force-recreate`

# Build everything from scratch, without using cache, ouch !
docker-compose build --no-cache && docker-compose up --force-recreate
```

# Running in uat local
cd admin-portal && yarn install && ng build --delete-output-path --configuration=uat --optimization --progress --aot --build-optimizer
docker-compose -f docker-compose-uat.yml up -d

# Running in prod local
docker-compose -f docker-compose-prod.yml build && docker-compose -f docker-compose-prod.yml up -d