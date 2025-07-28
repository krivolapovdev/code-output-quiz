# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle ./

COPY user-service/build.gradle.kts ./user-service/build.gradle.kts

RUN gradle --no-daemon :user-service:dependencies

COPY user-service/src ./user-service/src

RUN gradle --no-daemon :user-service:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/user-service/build/libs/*.jar ./user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]
