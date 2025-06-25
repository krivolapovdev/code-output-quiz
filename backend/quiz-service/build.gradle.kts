plugins {
    id("org.flywaydb.flyway") version "11.9.1"
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.9.1")
    }
}

val openAiVersion = "1.0.0-M6"
val springdocVersion = "2.8.8"
val postgresR2dbcVersion = "1.0.7.RELEASE"
val postgresVersion = "42.7.7"
val flywayVersion = "11.9.1"
val jacksonVersion = "2.19.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:$openAiVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springdocVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    runtimeOnly("org.postgresql:r2dbc-postgresql:$postgresR2dbcVersion")
    runtimeOnly("org.postgresql:postgresql:$postgresVersion")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/quiz_db"
    user = "postgres"
    password = "root"

    val profile = System.getenv("SPRING_PROFILES_ACTIVE") ?: "prod"
    cleanDisabled = profile != "dev"
}
