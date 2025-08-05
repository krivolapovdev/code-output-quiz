val jjwtVersion = "0.12.6"

dependencies {
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.projectreactor:reactor-core")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework:spring-webflux")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.slf4j:slf4j-api")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.projectreactor.kafka:reactor-kafka")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
