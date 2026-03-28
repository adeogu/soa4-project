# ============================================================
# Dockerfile - Actor Service
# Multi-stage build to keep the final image small.
# ============================================================

# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (matches server.port in application.properties)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]