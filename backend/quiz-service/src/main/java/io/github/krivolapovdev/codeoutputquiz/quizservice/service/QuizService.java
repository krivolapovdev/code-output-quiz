package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.config.cache.CacheNames;
import io.github.krivolapovdev.codeoutputquiz.quizservice.config.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.exception.QuizNotFoundException;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizViewRepository;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
  private final QuizViewRepository quizViewRepository;
  private final QuizMapper quizMapper;

  public Mono<QuizResponse> getRandomQuiz(QuizRequest quizRequest) {
    log.info(
        "Requesting random quiz for language: {}, level: {}",
        quizRequest.programmingLanguage(),
        quizRequest.difficultyLevel());
    return quizViewRepository
        .findRandomQuizView(quizRequest.programmingLanguage(), quizRequest.difficultyLevel())
        .doOnNext(quiz -> log.info("Random quiz fetched from DB: {}", quiz))
        .map(quizMapper::toResponse);
  }

  @Cacheable(value = CacheNames.QUIZ_CACHE, key = "#id")
  public Mono<QuizResponse> getQuizById(UUID id) {
    log.info("Requesting quiz by id: {}", id);
    return quizViewRepository
        .findById(id)
        .doOnNext(quiz -> log.info("Quiz fetched from DB: {}", quiz))
        .map(quizMapper::toResponse)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Quiz not found with id: " + id)));
  }

  public @NonNull Mono<QuizResponse> getUserUnsolvedQuiz(
      @NonNull ProgrammingLanguage programmingLanguage,
      @NonNull DifficultyLevel difficultyLevel,
      @NonNull Authentication authentication) {
    AuthDetails authDetails = (AuthDetails) authentication.getDetails();
    UUID userId = authDetails.userId();

    log.info(
        "Requesting unsolved quiz for user {}: {}, {}",
        userId,
        programmingLanguage,
        difficultyLevel);
    return quizViewRepository
        .findUserUnsolvedQuiz(programmingLanguage, difficultyLevel, userId)
        .doOnNext(quiz -> log.info("Found unsolved quiz {} for user {}", quiz.getId(), userId))
        .map(quizMapper::toResponse)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Quiz not found for user: " + userId)));
  }
}
