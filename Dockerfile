# Stage 1: Build the application with Maven
FROM maven:3.9.6-eclipse-temurin-21 AS mvn
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with the application JAR
FROM openjdk:21-slim
COPY --from=mvn '/app/target/task-list-0.0.1-SNAPSHOT.jar' '/app/task-list-0.0.1-SNAPSHOT.jar'
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/task-list-0.0.1-SNAPSHOT.jar"]