val openAiVersion = "1.0.0-M6"
val springdocVersion = "2.8.8"
val caffeineVersion = "3.2.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:$openAiVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springdocVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
