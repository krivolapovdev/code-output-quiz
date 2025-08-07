package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserQuizReactionRequest;
import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserQuizReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserQuizReactionController {
  private final UserQuizReactionService userQuizReactionService;

  @PostMapping("/me/quiz-reactions")
  @PreAuthorize("isAuthenticated()")
  public Mono<Void> reactToQuiz(
      @Valid @RequestBody UserQuizReactionRequest request, Authentication authentication) {
    return userQuizReactionService.reactToQuiz(request, authentication);
  }
}
