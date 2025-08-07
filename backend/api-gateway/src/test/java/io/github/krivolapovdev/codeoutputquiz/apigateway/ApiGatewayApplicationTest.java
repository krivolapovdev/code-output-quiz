package io.github.krivolapovdev.codeoutputquiz.apigateway;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081)
@TestPropertySource(
    properties = {
      "spring.cloud.discovery.enabled=false",
      "spring.cloud.gateway.routes[0].id=test-service",
      "spring.cloud.gateway.routes[0].uri=http://localhost:8081",
      "spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v*/test/**"
    })
class ApiGatewayApplicationTest {
  @Autowired private WebTestClient webTestClient;

  @BeforeEach
  void setup() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlPathMatching("/api/v./test/.*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"status\": \"ok\"}")));
  }

  @Test
  void shouldForwardRequestAndReturnExpectedJsonResponse() {
    webTestClient
        .get()
        .uri("/api/v1/test/sample")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("ok");
  }
}
