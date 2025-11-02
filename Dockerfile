# Stage 1: build jar
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x mvnw

# Copy source code
COPY src src

# Build the jar
RUN ./mvnw clean package -DskipTests

# Stage 2: create lightweight runtime image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/futsal-0.0.1-SNAPSHOT.jar app.jar

# Expose port and run
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
