## Use an official Java runtime
#FROM eclipse-temurin:21-jdk-alpine
#
## Set working directory
#WORKDIR /app
#
## Copy the project jar (replace with your jar name)
#COPY target/futsal-0.0.1-SNAPSHOT.jar app.jar
#
## Expose port 8080 (or your Spring Boot server.port)
#EXPOSE 8080
#
## Command to run the jar
#CMD ["java", "-jar", "app.jar"]


FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn

# Copy source code
COPY src src

# Build jar inside Docker
RUN ./mvnw clean package -DskipTests

# Copy the built jar
COPY target/futsal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
