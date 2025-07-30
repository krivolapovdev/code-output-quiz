package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.userservice.repository.UserQuizReactionRepository;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserQuizReactionRequest;
import io.github.krivolapovdev.codeoutputquiz.userservice.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQuizReactionService {
  private final UserQuizReactionRepository userQuizReactionRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public Mono<Void> reactToQuiz(@NonNull UserQuizReactionRequest request) {
    log.info(
        "Received quiz reaction request: quizId={}, liked={}", request.quizId(), request.liked());

    return ReactiveSecurityContextHolder.getContext()
        .map(ctx -> (String) ctx.getAuthentication().getCredentials())
        .map(jwtTokenProvider::extractUserIdFromToken)
        .doOnNext(
            userId ->
                log.info(
                    "Saving quiz reaction for userId={}, quizId={}, liked={}",
                    userId,
                    request.quizId(),
                    request.liked()))
        .flatMap(
            userId ->
                userQuizReactionRepository.saveUserQuizReaction(
                    userId, request.quizId(), request.liked()));
  }
}
