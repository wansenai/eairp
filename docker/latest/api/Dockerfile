# First stage: Build the application using Maven
FROM maven:3.9-sapmachine-21 AS builder
WORKDIR /build

# Copy the root pom.xml and download dependencies
COPY pom.xml ./
COPY api/pom.xml api/pom.xml
COPY service/pom.xml service/pom.xml
COPY dao/pom.xml dao/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY middleware/pom.xml middleware/pom.xml
COPY utils/pom.xml utils/pom.xml
COPY plugins/pom.xml plugins/pom.xml

# Run mvn verify to download all dependencies, without running tests
RUN mvn verify --fail-never

# Copy the source code and build the application
COPY api/src api/src
COPY service/src service/src
COPY dao/src dao/src
COPY domain/src domain/src
COPY middleware/src middleware/src
COPY utils/src utils/src

# Build the project and create the final JAR file
RUN mvn package -DskipTests

# Second stage: Prepare the runtime image
FROM openjdk:21 AS runtime
WORKDIR /application

# Copy the built JAR from the builder stage
COPY --from=builder /build/api/target/eairp-core.jar application.jar

# Extract the JAR using layertools
RUN java -Djarmode=layertools -jar application.jar extract

# Final stage: Create the final image
FROM openjdk:21
WORKDIR /application

# Copy the extracted layers
COPY --from=runtime /application/dependencies/ ./
COPY --from=runtime /application/spring-boot-loader/ ./
COPY --from=runtime /application/snapshot-dependencies/ ./
COPY --from=runtime /application/application/ ./

# Expose the necessary port
EXPOSE 8088

# Set the entrypoint
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]