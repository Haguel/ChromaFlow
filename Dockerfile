# Stage 1: Common dependencies layer for all modules
FROM maven:3.9-eclipse-temurin-21-alpine AS dependencies

WORKDIR /app

# Copy the parent pom.xml
COPY pom.xml .

# Copy all module poms to leverage caching
COPY shared/pom.xml ./shared/
COPY job_ingestion_service/pom.xml ./job_ingestion_service/
COPY media_processing_worker/pom.xml ./media_processing_worker/
COPY job_status_service/pom.xml ./job_status_service/
COPY gateway/pom.xml ./gateway/
COPY storage_abstraction_service/pom.xml ./storage_abstraction_service/

# Download all dependencies once. This layer will be cached.
RUN mvn -B dependency:go-offline

# ---------------------------------------------------------------------

# Stage 2: Builder stage for a specific module
FROM dependencies AS builder

# This build argument will be passed from docker-compose.yml
ARG MODULE_NAME

# Copy all source code from the project root
COPY . .

# Build the specified module and its local dependencies
RUN mvn -pl ${MODULE_NAME} -am clean install -DskipTests

# ---------------------------------------------------------------------

# Stage 3: Final runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Another build argument to define the application port
ARG EXPOSE_PORT=8080
EXPOSE ${EXPOSE_PORT}

# This build argument will point to the JAR file to be run
ARG MODULE_NAME

# Copy the final JAR from the builder stage
COPY --from=builder /app/${MODULE_NAME}/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]