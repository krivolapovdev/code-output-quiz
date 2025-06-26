package io.github.nellshark.codeoutputquiz.authservice.controller;

import io.github.nellshark.codeoutputquiz.authservice.request.AuthRequest;
import io.github.nellshark.codeoutputquiz.authservice.request.RegistrationRequest;
import io.github.nellshark.codeoutputquiz.authservice.response.AuthResponse;
import io.github.nellshark.codeoutputquiz.authservice.response.RegistrationResponse;
import io.github.nellshark.codeoutputquiz.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public Mono<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
    return authService.register(registrationRequest);
  }

  @PostMapping("/login")
  public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
    return authService.login(authRequest);
  }
}
