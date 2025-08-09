# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle ./

COPY common/build.gradle.kts ./common/build.gradle.kts

COPY api-gateway/build.gradle.kts ./api-gateway/build.gradle.kts

RUN gradle --no-daemon :api-gateway:dependencies

COPY common/src ./common/src

COPY api-gateway/src ./api-gateway/src

RUN gradle --no-daemon :api-gateway:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/api-gateway/build/libs/*.jar ./api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
