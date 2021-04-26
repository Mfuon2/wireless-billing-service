# Stage 1
# pull node from alpine
FROM node:14.15.0-alpine as node

# Maintainer Labels
LABEL project="Vuka Wireless Billing"
LABEL maintainer="mfolee@gmail.com"

# define app working directory
WORKDIR /usr/src/app

# copy dependancies file from local
COPY package*.json ./

# install yarn package installer
RUN npm install yarn

# copy project files to our image
COPY . .

# install node packages for the portal
RUN yarn install

# build the project
RUN yarn build

# Stage 2
# Install the load balancer nginx from alpine
FROM nginx:1.13.12-alpine

# copy the build from a previous image created for admin portal
COPY --from=node /usr/src/app/dist/admin-dashboard /usr/share/nginx/html

# copy the nginx configurations to the deocker image
COPY ./nginx.conf /etc/nginx/conf.d/default.conf