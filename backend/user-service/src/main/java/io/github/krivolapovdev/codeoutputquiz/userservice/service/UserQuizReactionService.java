package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserQuizReaction;
import io.github.krivolapovdev.codeoutputquiz.userservice.repository.UserQuizReactionRepository;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserQuizReactionRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQuizReactionService {
  private final UserQuizReactionRepository userQuizReactionRepository;

  public Mono<Void> reactToQuiz(
      @NonNull UserQuizReactionRequest request, @NonNull Authentication authentication) {
    log.info("Received quiz reaction request: {}", request);

    AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
    UUID userId = authPrincipal.id();
    UserQuizReaction reaction = new UserQuizReaction(userId, request.quizId(), request.liked());

    return userQuizReactionRepository
        .saveUserQuizReaction(reaction)
        .doOnSuccess(ignored -> log.info("Quiz reaction saved successfully"))
        .doOnError(ignored -> log.error("Failed to save quiz reaction"));
  }
}
