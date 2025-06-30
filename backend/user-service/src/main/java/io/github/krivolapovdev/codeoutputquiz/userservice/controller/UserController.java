package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  @GetMapping("/secure")
  @PreAuthorize("hasRole('USER')")
  public Mono<String> temp() {
    return Mono.just("log");
  }
}
