## 1. BUILD STAGE
# base image
FROM node:14.15.0-alpine AS build

LABEL maintainer="Mfuon Leonard" MAINTAINER="mfuon leonard"
LABEL application="admin-portal"

EXPOSE 4200
ENV APP_HOME=/home/node/app
RUN mkdir -p $APP_HOME && chown -R node:node $APP_HOME
WORKDIR $APP_HOME

# update the alpine image and install curl
RUN apk update && apk add curl

# installing Alpine Dependencies, but the context for the command from `npm install` is explained above
RUN apk add --no-cache --virtual .build-deps1 g++ gcc libgcc libstdc++ linux-headers make python && \
    apk add --no-cache --virtual .npm-deps npm cairo-dev jpeg-dev libjpeg-turbo-dev pango pango-dev && \
    apk add bash

RUN echo 'http://dl-cdn.alpinelinux.org/alpine/v3.9/main' >> /etc/apk/repositories
RUN echo 'http://dl-cdn.alpinelinux.org/alpine/v3.9/community' >> /etc/apk/repositories

RUN apk upgrade --update
RUN apk add --no-cache mongodb
RUN apk add --no-cache mongodb-tools && \
    rm -rf /var/cache/apk/*

# Set non-root user and folder
USER node
# Copy source code (and all other relevant files)
COPY --chown=node:node . ./
RUN npm install --frozen-lockfile
# Build code
RUN npm build

RUN chmod 777 /home/node/app/entrypoint.sh
ENTRYPOINT ["/home/node/app/entrypoint.sh"]