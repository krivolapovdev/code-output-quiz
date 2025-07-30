package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.SolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.SolvedQuizRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolvedQuizService {
  private final SolvedQuizRepository solvedQuizRepository;

  public @NonNull Mono<Void> saveUserSolvedQuiz(@NonNull UUID userId, @NonNull UUID quizId) {
    log.info("Saving solved quiz for user {}: {}", userId, quizId);
    SolvedQuiz solvedQuiz = new SolvedQuiz(userId, quizId);
    return solvedQuizRepository
        .save(solvedQuiz)
        .doOnNext(ignored -> log.info("Saved solved quiz for user {}: {}", userId, quizId))
        .doOnError(error -> log.error("Failed to save solved quiz for user {}", userId, error))
        .then();
  }
}
