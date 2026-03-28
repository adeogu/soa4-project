# ============================================================
# Dockerfile - Movie Service
# Multi-stage build:
#   Stage 1 (build): Maven compiles and packages the JAR
#   Stage 2 (run):   Lightweight JRE image runs the JAR
# Multi-stage keeps the final image small (no Maven/source in it)
# ============================================================

# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first so Docker caches the dependency download layer.
# If pom.xml hasn't changed, this expensive step is skipped on rebuild.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Expose port 8081 (matches server.port in application.properties)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]