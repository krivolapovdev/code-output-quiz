package io.github.krivolapovdev.codeoutputquiz.authservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.TestSecurityConfig;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {
  @Autowired private WebTestClient webTestClient;

  @MockitoBean private AuthService authService;

  @Test
  void shouldRegisterUserAndReturnAuthResponse() {
    var request = new AuthRequest("test@example.com", "password");
    var response = new AuthResponse();

    when(authService.register(any()))
        .thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(response)));

    webTestClient
        .post()
        .uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isCreated();

    ArgumentCaptor<AuthRequest> captor = ArgumentCaptor.forClass(AuthRequest.class);
    verify(authService).register(captor.capture());

    AuthRequest captured = captor.getValue();
    assertThat(captured.email()).isEqualTo("test@example.com");
    assertThat(captured.password()).isEqualTo("password");
  }

  @Test
  void shouldLoginUserAndReturnAuthResponse() {
    var request = new AuthRequest("test@example.com", "password");
    var response = new AuthResponse();

    when(authService.login(any())).thenReturn(Mono.just(ResponseEntity.ok(response)));

    webTestClient
        .post()
        .uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();

    ArgumentCaptor<AuthRequest> captor = ArgumentCaptor.forClass(AuthRequest.class);
    verify(authService).login(captor.capture());

    AuthRequest captured = captor.getValue();
    assertThat(captured.email()).isEqualTo("test@example.com");
    assertThat(captured.password()).isEqualTo("password");
  }

  @Test
  void shouldLogoutUserAndReturnNoContent() {
    when(authService.logout()).thenReturn(Mono.just(ResponseEntity.noContent().build()));

    webTestClient.post().uri("/api/v1/auth/logout").exchange().expectStatus().isNoContent();

    verify(authService).logout();
  }
}
