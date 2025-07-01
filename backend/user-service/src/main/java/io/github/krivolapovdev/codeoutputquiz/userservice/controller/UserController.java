package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/me/solved-quizzes")
  @PreAuthorize("isAuthenticated()")
  public Flux<String> getUserSolvedQuizzes() {
    return userService.getUserSolvedQuizzes();
  }
}
