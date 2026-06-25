# Étape 1 : Build du serveur avec Gradle
FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle :server:buildFatJar --no-daemon

# Étape 2 : Image légère pour exécuter le serveur
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/server/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]