val openAiVersion = "1.0.0-M6"
val springdocVersion = "2.8.9"
val postgresR2dbcVersion = "1.0.7.RELEASE"
val postgresVersion = "42.7.7"
val flywayVersion = "11.9.1"
val jacksonVersion = "2.19.1"
val mapstructVersion = "1.6.3"
val jjwtVersion = "0.12.6"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:$openAiVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springdocVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.postgresql:r2dbc-postgresql:$postgresR2dbcVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-reactive")

    runtimeOnly("org.postgresql:postgresql:$postgresVersion")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
