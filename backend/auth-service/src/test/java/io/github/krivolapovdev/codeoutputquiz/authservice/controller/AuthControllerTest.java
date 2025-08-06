package io.github.krivolapovdev.codeoutputquiz.authservice.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.TestSecurityConfig;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {
  @Autowired private WebTestClient webTestClient;
  @MockitoBean private AuthService authService;

  @Test
  void shouldRegisterUserAndReturnAuthResponse() {
    var request = new AuthRequest("test@example.com", "password");
    var response = new AuthResponse();

    when(authService.register(request))
        .thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(response)));

    webTestClient
        .post()
        .uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isCreated();

    verify(authService).register(request);
  }

  @Test
  void shouldLoginUserAndReturnAuthResponse() {
    var request = new AuthRequest("test@example.com", "password");
    var response = new AuthResponse();

    when(authService.login(request)).thenReturn(Mono.just(ResponseEntity.ok(response)));

    webTestClient
        .post()
        .uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();

    verify(authService).login(request);
  }

  @Test
  void shouldLogoutUserAndReturnNoContent() {
    when(authService.logout()).thenReturn(Mono.just(ResponseEntity.noContent().build()));

    webTestClient.post().uri("/api/v1/auth/logout").exchange().expectStatus().isNoContent();

    verify(authService).logout();
  }
}
