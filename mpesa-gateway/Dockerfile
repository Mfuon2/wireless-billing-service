#Software Elegance 2020

#NB: The first time this build will take long but subsequent times docker cache will be used 

ARG BUILD_IMAGE=maven:3.5-jdk-11
ARG RUNTIME_IMAGE=openjdk:11-jdk-slim
ARG DOCKER_SPRING_PROFILES_ACTIVE

#############################################################################################
###                Pull all maven dependencies                   ###
#############################################################################################
FROM ${BUILD_IMAGE} as dependencies

ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=

COPY pom.xml ./

RUN mvn -B dependency:go-offline \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT}
#############################################################################################

#############################################################################################
###              Build spring boot app using maven               ###
#############################################################################################
FROM dependencies as build

ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=

COPY src ./src

RUN mvn -B clean package -DskipTests \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT}
#############################################################################################

#############################################################################################
###                Run Sonar analysis when working outside a ci/cd pipeline - Optional             ###
#############################################################################################
FROM build

ARG SONAR_ENABLED=false
ARG SONAR_URL=
ARG SONAR_ORGANIZATION=
ARG SONAR_USERNAME=
ARG SONAR_PASSWORD=
ARG SONAR_BRANCH=

RUN if [ "$SONAR_ENABLED" = "true" ] ; \
    then mvn -B sonar:sonar \
        -Dsonar.host.url=${SONAR_URL} \
        -Dsonar.organization=${SONAR_ORGANIZATION} \
        -Dsonar.branch.name=${SONAR_BRANCH} \
        -Dsonar.login=${SONAR_USERNAME} \
        -Dsonar.password=${SONAR_PASSWORD}; \
    fi
#############################################################################################

#############################################################################################
### Run the service built in previous stage ###
#############################################################################################
FROM ${RUNTIME_IMAGE}

ENV SPRING_PROFILES_ACTIVE $DOCKER_SPRING_PROFILES_ACTIVE
ENV TZ Africa/Nairobi

# # Add Maintainer info
LABEL project="Vuka Wireless Billing"
LABEL maintainer="itambo.ibrahim@gmail.com"

# # Copy from previous build
COPY --from=build /target/*.jar /app.jar

# # Add a volume pointing to /tmp
VOLUME /tmp

# # 
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

#############################################################################################

#docker build -t zeguru/mpesa-gateway:beta .
#docker run -e "SPRING_PROFILES_ACTIVE=dev" --name mpesa-gateway --restart unless-stopped -d -p 8080:8080 zeguru/mpesa-gateway:beta
