# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY backend/build.gradle.kts backend/settings.gradle.kts backend/gradle ./

COPY backend/auth-service/build.gradle.kts ./auth-service/build.gradle.kts

RUN gradle --no-daemon :auth-service:dependencies

COPY backend/auth-service/src ./auth-service/src

RUN gradle --no-daemon :auth-service:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/auth-service/build/libs/*.jar ./auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
