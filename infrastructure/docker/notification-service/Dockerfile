# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle ./

COPY notification-server/build.gradle.kts ./notification-server/build.gradle.kts

RUN gradle --no-daemon :notification-server:dependencies

COPY notification-server/src ./notification-server/src

RUN gradle --no-daemon :notification-server:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/notification-server/build/libs/*.jar ./notification-server.jar

ENTRYPOINT ["java", "-jar", "notification-server.jar"]
