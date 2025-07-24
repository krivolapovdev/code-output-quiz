# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle ./

COPY notification-service/build.gradle.kts ./notification-service/build.gradle.kts

RUN gradle --no-daemon :notification-service:dependencies

COPY notification-service/src ./notification-service/src

RUN gradle --no-daemon :notification-service:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/notification-service/build/libs/*.jar ./notification-service.jar

ENTRYPOINT ["java", "-jar", "notification-service.jar"]
