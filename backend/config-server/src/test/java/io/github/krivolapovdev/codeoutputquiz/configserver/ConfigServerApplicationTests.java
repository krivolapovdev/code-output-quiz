package io.github.krivolapovdev.codeoutputquiz.configserver;

import java.time.Duration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigServerApplicationTests {
  @LocalServerPort private int port;

  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    webTestClient =
        WebTestClient.bindToServer()
            .responseTimeout(Duration.ofMinutes(1))
            .baseUrl("http://localhost:" + port)
            .build();
  }

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
