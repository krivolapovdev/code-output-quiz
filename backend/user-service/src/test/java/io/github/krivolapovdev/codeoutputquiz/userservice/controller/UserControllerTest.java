package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.userservice.response.UserResponse;
import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {
  @Autowired private WebTestClient webTestClient;
  @MockitoBean private UserService userService;

  @Test
  void shouldReturnCurrentUser() {
    UserResponse expected = new UserResponse(UUID.randomUUID(), "user@example.com");

    when(userService.getCurrentUser(any())).thenReturn(Mono.just(expected));

    webTestClient
        .get()
        .uri("/api/v1/users/me")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(UserResponse.class)
        .value(response -> assertThat(response).isEqualTo(expected));
  }
}
