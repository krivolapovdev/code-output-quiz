package io.github.nellshark.codeoutputquiz.authservice.controller;

import io.github.nellshark.codeoutputquiz.authservice.entity.User;
import io.github.nellshark.codeoutputquiz.authservice.request.RegistrationRequest;
import io.github.nellshark.codeoutputquiz.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
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
  public Mono<User> register(@RequestBody RegistrationRequest registrationRequest) {
    return authService.register(registrationRequest);
  }
}
