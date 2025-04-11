FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Copy the project source
COPY src src

# Make the mvnw executable
RUN chmod +x ./mvnw

# Package the application
RUN ./mvnw package -DskipTests

# Create a slim java runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /workspace/app/target/*.jar app.jar

# Set the startup command to execute the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
