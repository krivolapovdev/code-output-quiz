import com.diffplug.gradle.spotless.SpotlessExtension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java")
    id("com.diffplug.spotless") version "7.0.4" apply false
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

val micrometerRegistryPrometheusVersion by extra("1.15.0")
val lombokVersion by extra("1.18.38")
val springCloudVersion by extra("2025.0.0-RC1")
val lokiLogbackAppenderVersion by extra("2.0.0")
val zipkinReporterBraveVersion by extra("3.5.1")
val micrometerTracingBridgeBraveVersion by extra("1.5.1")

subprojects {
    group = "io.github.krivolapovdev.codeoutputquiz"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("io.micrometer:micrometer-registry-prometheus:$micrometerRegistryPrometheusVersion")
        implementation("com.github.loki4j:loki-logback-appender:$lokiLogbackAppenderVersion")
        implementation("io.zipkin.reporter2:zipkin-reporter-brave:$zipkinReporterBraveVersion")
        implementation("io.micrometer:micrometer-tracing-bridge-brave:$micrometerTracingBridgeBraveVersion")

        compileOnly("org.projectlombok:lombok:$lombokVersion")
        annotationProcessor("org.projectlombok:lombok:$lombokVersion")
        testCompileOnly("org.projectlombok:lombok:$lombokVersion")
        testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    }

    extensions.configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    extensions.configure<SpotlessExtension> {
        java {
            googleJavaFormat()
            target("src/**/*.java")
        }
    }
}
