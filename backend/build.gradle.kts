plugins {
    id("java")
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

subprojects {
    group = "io.github.nellshark"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    extra["springCloudVersion"] = "2025.0.0-RC1"

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
