# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY backend/build.gradle.kts backend/settings.gradle.kts backend/gradle ./

COPY backend/config-server/build.gradle.kts ./config-server/build.gradle.kts

RUN gradle --no-daemon :config-server:dependencies

COPY backend/config-server/src ./config-server/src

RUN gradle --no-daemon :config-server:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/config-server/build/libs/*.jar ./config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]
