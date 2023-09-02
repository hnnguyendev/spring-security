# syntax=docker/dockerfile:1

# Start your image with a jdk base image
FROM eclipse-temurin:11-jdk-jammy
# Create an application directory
RUN mkdir -p /app
# Set the /app directory as the working directory for any command that follows
WORKDIR /app
# Copy local directories to the working directory of our docker image (/app)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Install dependencies, build the app
RUN ./mvnw dependency:go-offline
COPY src src
# Specify that the application in the container listens on port 8080
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]
