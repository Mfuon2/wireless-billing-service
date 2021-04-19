# Download base image alpine 3.13.5
FROM alpine:3.13.5

# LABEL about the custom image
LABEL maintainer="mfolee@gmail.com"
LABEL version="0.1"
LABEL description="This is custom Docker Image for1 Admin Portal"

# Update Ubuntu Software repository
RUN apk update

# Install git, nodejs and npm
RUN apk add --no-cache git && apk add --update nodejs-current npm
RUN addgroup -S node && adduser -S node -G node
USER node

# Define the ENV variable
ENV admin_portal_dir /opt/frontend/admin-portal

# Enable PHP-fpm on nginx virtualhost configuration
COPY --chown=node:node . ${admin_portal_dir}

# Volume configuration
# VOLUME ["/var/www/html"]

# Run NPM install
RUN cd ${admin_portal_dir} && npm install

# Expose Port for the Application
EXPOSE 4200 4201 4202
