# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle ./

COPY user-server/build.gradle.kts ./user-server/build.gradle.kts

RUN gradle --no-daemon :user-server:dependencies

COPY user-server/src ./user-server/src

RUN gradle --no-daemon :user-server:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/user-server/build/libs/*.jar ./user-server.jar

ENTRYPOINT ["java", "-jar", "user-server.jar"]
