package io.github.krivolapovdev.codeoutputquiz.authservice.controller;

import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.TokenService;
import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {
  private final TokenService tokenService;

  @PostMapping("/refresh")
  public Mono<ResponseEntity<AuthResponse>> refresh(
      @CookieValue(CookieNames.REFRESH_TOKEN) String refreshToken) {
    return tokenService.refreshToken(refreshToken);
  }
}
