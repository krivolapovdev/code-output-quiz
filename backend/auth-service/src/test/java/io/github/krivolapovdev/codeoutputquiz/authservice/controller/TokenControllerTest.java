package io.github.krivolapovdev.codeoutputquiz.authservice.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.TestSecurityConfig;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.TokenService;
import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(TokenController.class)
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class TokenControllerTest {
  @Autowired private WebTestClient webTestClient;

  @MockitoBean private TokenService tokenService;

  @Test
  void shouldRefreshTokenAndReturnAuthResponse() {
    var refreshToken = "test-refresh-token";
    var response = new AuthResponse();

    when(tokenService.refreshToken(any())).thenReturn(Mono.just(ResponseEntity.ok(response)));

    webTestClient
        .post()
        .uri("/api/v1/tokens/refresh")
        .cookie(CookieNames.REFRESH_TOKEN, refreshToken)
        .exchange()
        .expectStatus()
        .isOk();

    ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
    verify(tokenService).refreshToken(tokenCaptor.capture());

    assertThat(tokenCaptor.getValue()).isEqualTo(refreshToken);
  }
}
