package io.github.krivolapovdev.codeoutputquiz.configserver;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "PT2M")
class ConfigServerApplicationTests {
  @Autowired private WebTestClient webTestClient;

  @Test
  void shouldReturnConfigFromGit() {
    webTestClient
        .get()
        .uri("/application/default")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo("application")
        .jsonPath("$.propertySources")
        .isArray()
        .jsonPath("$.propertySources.length()")
        .value(Matchers.greaterThan(0));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "api-gateway",
        "auth-service",
        "eureka-server",
        "notification-service",
        "quiz-service",
        "user-service"
      })
  void shouldReturnConfigForService(String serviceName) {
    webTestClient
        .get()
        .uri("/" + serviceName + "/default")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo(serviceName)
        .jsonPath("$.propertySources")
        .isArray()
        .jsonPath("$.propertySources.length()")
        .value(Matchers.greaterThan(0));
  }
}
