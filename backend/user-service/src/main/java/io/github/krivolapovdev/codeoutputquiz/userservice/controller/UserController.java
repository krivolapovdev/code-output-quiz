package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import io.github.krivolapovdev.codeoutputquiz.userservice.response.UserResponse;
import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public Mono<UserResponse> getCurrentUser(Authentication authentication) {
    return userService.getCurrentUser(authentication);
  }
}
