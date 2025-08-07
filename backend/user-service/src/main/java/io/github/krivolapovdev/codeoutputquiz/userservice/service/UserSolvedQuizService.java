package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserSolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.userservice.notifier.QuizSolvedNotifier;
import io.github.krivolapovdev.codeoutputquiz.userservice.repository.UserSolvedQuizRepository;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserSolvedQuizRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSolvedQuizService {
  private final UserSolvedQuizRepository userSolvedQuizRepository;
  private final QuizSolvedNotifier quizSolvedNotifier;

  public Mono<Void> addUserSolvedQuiz(
      @NonNull UserSolvedQuizRequest request, @NonNull Authentication authentication) {
    log.info("Received user solved quiz request: {}", request);

    AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
    UUID userId = authPrincipal.id();

    UserSolvedQuiz userSolvedQuiz =
        new UserSolvedQuiz(request.quizId(), userId, request.selectedAnswer());

    return userSolvedQuizRepository
        .addUserSolvedQuiz(userSolvedQuiz)
        .doOnSuccess(ignored -> log.info("User solved quiz saved successfully"))
        .doOnError(error -> log.error("Failed to save user solved quiz", error))
        .thenReturn(new QuizSolvedEvent(userId, request.quizId()))
        .flatMap(quizSolvedNotifier::sendQuizSolvedEvent)
        .then();
  }
}
