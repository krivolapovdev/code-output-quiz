val springdocVersion = "2.8.9"
val postgresVersion = "42.7.7"
val postgresR2dbcVersion = "1.0.7.RELEASE"
val flywayVersion = "11.9.1"
val jjwtVersion = "0.12.6"
val caffeineVersion = "3.2.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-reactive")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${springdocVersion}")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:${caffeineVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")

    runtimeOnly("org.postgresql:postgresql:${postgresVersion}")
    runtimeOnly("org.postgresql:r2dbc-postgresql:${postgresR2dbcVersion}")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:${flywayVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
}
