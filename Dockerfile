# ─── Stage 1 : Build ───────────────────────────────────────────────────────────
# Même image que l'original Render — aucune régression de déploiement.
FROM gradle:8.13-jdk21 AS build
WORKDIR /app

# 1. Fichiers de configuration Gradle (couche stable : change rarement).
#    Copiés en premier pour maximiser le cache Docker entre deux builds.
COPY gradle/ gradle/
COPY gradlew gradlew.bat gradle.properties settings.gradle.kts build.gradle.kts ./

# 2. Descripteurs de chaque module (requis pour que Gradle configure tous
#    les sous-projets déclarés dans settings.gradle.kts).
COPY server/build.gradle.kts server/
COPY shared/build.gradle.kts  shared/
COPY composeApp/build.gradle.kts composeApp/

# 3. Sources des seuls modules compilés pour le serveur.
COPY server/src/  server/src/
COPY shared/src/  shared/src/

# Build du fat JAR via le plugin Ktor.
# --mount=type=cache : le répertoire ~/.gradle est persisté entre les builds
# (évite de re-télécharger les dépendances à chaque docker build).
# Requiert BuildKit (activé par défaut sur Docker >= 23).
RUN --mount=type=cache,target=/root/.gradle,sharing=locked \
    ./gradlew :server:buildFatJar --no-daemon

# ─── Stage 2 : Runtime ─────────────────────────────────────────────────────────
# Image JRE Alpine : ~100 Mo de moins que la variante Debian.
# Aucun Gradle, aucune source — uniquement ce qui est necessaire a l'execution.
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# curl est necessaire pour le HEALTHCHECK.
RUN apk add --no-cache curl

# Seul le fat JAR provenant du stage build est copie dans l'image finale.
COPY --from=build /app/server/build/libs/*.jar app.jar

# Le port ecoute par defaut (peut etre surcharge via la variable PORT).
EXPOSE 8080

# Verifie que le serveur repond toutes les 30 s.
# GET / retourne 200 "KamerStay API" sans acces MongoDB — ideal comme probe.
# --start-period=30s laisse le temps au serveur de demarrer et de seeder la DB.
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# ENTRYPOINT (exec form) : les signaux OS (SIGTERM) sont transmis directement
# a la JVM, ce qui permet un arret propre de Ktor.
# -XX:+UseContainerSupport  : JVM respecte les limites CPU/memoire du conteneur.
# -XX:MaxRAMPercentage=75.0 : heap max = 75 % de la RAM allouee au conteneur.
# -Dio.ktor.development=false : desactive le mode dev (rechargement a chaud).
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Dio.ktor.development=false", \
    "-jar", "app.jar"]