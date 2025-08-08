package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.SolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.SolvedQuizRepository;
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

  public @NonNull Mono<Void> saveSolvedQuiz(@NonNull SolvedQuiz solvedQuiz) {
    log.info("Saving solved quiz: {}", solvedQuiz);
    return solvedQuizRepository
        .save(solvedQuiz)
        .doOnNext(ignored -> log.info("Saved solved quiz: {}", solvedQuiz))
        .doOnError(error -> log.error("Failed to save solved quiz: {}", solvedQuiz, error))
        .then();
  }
}
