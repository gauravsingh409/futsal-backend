# Use an official Java runtime
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the project jar (replace with your jar name)
COPY target/futsal-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (or your Spring Boot server.port)
EXPOSE 8080

# Command to run the jar
CMD ["java", "-jar", "app.jar"]
