# First stage: Build the web application
FROM node:18.18.0 as build-web
WORKDIR /app/web

COPY ./web/package.json ./web/pnpm-lock.yaml ./
RUN npm config set registry https://registry.npmmirror.com
ENV NODE_OPTIONS --max-old-space-size=16384
RUN npm install pnpm -g

COPY ./web ./
RUN pnpm install --frozen-lockfile
RUN pnpm run build:docker

# Second stage: Build the API application
FROM maven:3.9-sapmachine-22 AS build-api
WORKDIR /build

# Copy the root pom.xml and download dependencies
COPY ./core/pom.xml ./
COPY ./core/api/pom.xml ./api/pom.xml
COPY ./core/service/pom.xml ./service/pom.xml
COPY ./core/dao/pom.xml ./dao/pom.xml
COPY ./core/domain/pom.xml ./domain/pom.xml
COPY ./core/middleware/pom.xml ./middleware/pom.xml
COPY ./core/utils/pom.xml ./utils/pom.xml
COPY ./core/plugins/pom.xml ./plugins/pom.xml

RUN mvn verify --fail-never

# Copy the source code and build the application
COPY ./core/api/src ./api/src
COPY ./core/service/src ./service/src
COPY ./core/dao/src ./dao/src
COPY ./core/domain/src ./domain/src
COPY ./core/middleware/src ./middleware/src
COPY ./core/utils/src ./utils/src

RUN mvn package -DskipTests

# Third stage: Install OpenJDK in a Debian-based image
FROM debian:bullseye-slim AS openjdk
RUN apt-get update && apt-get install -y openjdk-21-jdk && apt-get clean

# Fourth stage: Combine Nginx and OpenJDK
FROM nginx:1.23.4 AS final
WORKDIR /application

# Install OpenJDK manually
RUN apt-get update && apt-get install -y wget && \
    wget https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21%2B35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz && \
    tar -xzf OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz -C /usr/local && \
    rm OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz && \
    ln -s /usr/local/jdk-21+35 /usr/local/java && \
    apt-get clean

ENV JAVA_HOME /usr/local/java
ENV PATH $JAVA_HOME/bin:$PATH

# Copy the built API app from the second stage
COPY --from=build-api /build/api/target/eairp-core.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Copy the built web app from the first stage
COPY --from=build-web /app/web/dist /usr/share/nginx/html/dist
COPY --from=build-web /app/web/nginx.conf /etc/nginx/nginx.conf

# Copy the start script
COPY start.sh /start.sh
RUN chmod +x /start.sh

# Expose the necessary ports
EXPOSE 80
EXPOSE 8088

# Set the entrypoint
ENTRYPOINT ["/start.sh"]