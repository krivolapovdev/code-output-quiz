# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle ./

COPY common/build.gradle.kts ./common/build.gradle.kts

COPY config-server/build.gradle.kts ./config-server/build.gradle.kts

RUN gradle --no-daemon :config-server:dependencies

COPY common/src ./common/src

COPY config-server/src ./config-server/src

RUN gradle --no-daemon :config-server:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/config-server/build/libs/*.jar ./config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]
