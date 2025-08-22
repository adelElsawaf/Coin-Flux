# Multi-stage Dockerfile for Coin-Flux Spring Boot app
# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Cache dependencies by copying pom first
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:21-jre AS runtime
ENV APP_HOME=/app
WORKDIR ${APP_HOME}

# Create non-root user
RUN useradd -ms /bin/bash spring

# Copy fat jar from build stage
COPY --from=build /workspace/target/web-0.0.1-SNAPSHOT.jar ${APP_HOME}/app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Allow extra JVM options via JAVA_OPTS
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"

USER spring
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
