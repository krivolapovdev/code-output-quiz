package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import io.github.krivolapovdev.codeoutputquiz.userservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final AuthServiceClient authServiceClient;

  @GetMapping
  public Mono<String> ping() {
    return Mono.just("pong");
  }
}
