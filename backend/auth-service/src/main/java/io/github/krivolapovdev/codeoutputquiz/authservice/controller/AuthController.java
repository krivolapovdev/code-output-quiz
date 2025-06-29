package io.github.krivolapovdev.codeoutputquiz.authservice.controller;

import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @GetMapping("/secure")
  @PreAuthorize("hasRole('USER')")
  public Mono<String> securedEndpoint() {
    return Mono.just("You are authorized!");
  }

  @PostMapping("/register")
  public Mono<ResponseEntity<AuthResponse>> register(@Valid @RequestBody AuthRequest authRequest) {
    return authService.register(authRequest);
  }

  @PostMapping("/login")
  public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest authRequest) {
    return authService.login(authRequest);
  }

  @PostMapping("/refresh-token")
  public Mono<ResponseEntity<AuthResponse>> refreshToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return authService.refreshToken(token);
  }
}
