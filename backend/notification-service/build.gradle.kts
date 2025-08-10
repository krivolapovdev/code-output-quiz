val caffeineVersion = "3.2.0"
val mailjetVersion = "6.0.0"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.github.ben-manes.caffeine:caffeine:${caffeineVersion}")
    implementation("com.mailjet:mailjet-client:${mailjetVersion}")
    implementation("io.projectreactor.kafka:reactor-kafka")
}
