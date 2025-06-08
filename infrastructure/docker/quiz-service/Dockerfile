# Build stage
FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /app

COPY backend/build.gradle.kts backend/settings.gradle.kts backend/gradle ./

COPY backend/quiz-service/build.gradle.kts ./quiz-service/build.gradle.kts

RUN gradle --no-daemon :quiz-service:dependencies

COPY backend/quiz-service/src ./quiz-service/src

RUN gradle --no-daemon :quiz-service:bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/quiz-service/build/libs/*.jar ./quiz-service.jar

ENTRYPOINT ["java", "-jar", "quiz-service.jar"]
