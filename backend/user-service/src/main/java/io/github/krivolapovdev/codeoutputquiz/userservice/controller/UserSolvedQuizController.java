package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserSolvedQuizRequest;
import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserSolvedQuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserSolvedQuizController {
  private final UserSolvedQuizService userSolvedQuizService;

  @PostMapping("/me/solved-quizzes")
  @PreAuthorize("isAuthenticated()")
  public Mono<Void> addUserSolvedQuiz(
      @Valid @RequestBody UserSolvedQuizRequest userSolvedQuizRequest) {
    return userSolvedQuizService.addUserSolvedQuiz(userSolvedQuizRequest);
  }
}
