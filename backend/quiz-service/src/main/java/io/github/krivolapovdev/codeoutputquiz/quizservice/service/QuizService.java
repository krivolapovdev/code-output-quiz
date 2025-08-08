package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.common.cache.CacheNames;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.exception.QuizNotFoundException;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.AnswerChoicesJsonMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizViewRepository;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
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
  private final AnswerChoicesJsonMapper answerChoicesJsonMapper;

  public Mono<QuizResponse> getRandomQuiz(
      @NonNull ProgrammingLanguage programmingLanguage, @NonNull DifficultyLevel difficultyLevel) {
    log.info(
        "Requesting random quiz for language: {}, level: {}", programmingLanguage, difficultyLevel);
    return quizViewRepository
        .findRandomQuizView(programmingLanguage, difficultyLevel)
        .doOnNext(quiz -> log.info("Random quiz fetched from DB: {}", quiz))
        .map(quizMapper::toResponse)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Random Quiz not found")))
        .doOnError(error -> log.warn("Failed to get random quiz: {}", error.getMessage(), error));
  }

  @Cacheable(value = CacheNames.QUIZ_CACHE, key = "#id")
  public Mono<QuizResponse> getQuizById(UUID id) {
    log.info("Requesting quiz by id: {}", id);
    return quizViewRepository
        .findById(id)
        .doOnNext(quiz -> log.info("Quiz fetched from DB: {}", quiz))
        .map(quizMapper::toResponse)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Quiz not found with id: " + id)))
        .doOnError(error -> log.warn("Failed to get quiz by id: {}", error.getMessage(), error));
  }

  public @NonNull Mono<QuizResponse> getUserUnsolvedQuiz(
      @NonNull ProgrammingLanguage programmingLanguage,
      @NonNull DifficultyLevel difficultyLevel,
      @NonNull Authentication authentication) {
    AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
    UUID userId = authPrincipal.id();

    log.info(
        "Requesting unsolved quiz for user {}: {}, {}",
        userId,
        programmingLanguage,
        difficultyLevel);

    return quizViewRepository
        .findUserUnsolvedQuiz(programmingLanguage, difficultyLevel, userId)
        .doOnNext(quiz -> log.info("Found unsolved quiz {} for user {}", quiz.getId(), userId))
        .map(quizMapper::toResponse)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Quiz not found for user: " + userId)))
        .doOnError(error -> log.warn("Failed to get unsolved quiz: {}", error.getMessage(), error));
  }

  public Mono<QuizView> saveQuizWithChoices(@NonNull QuizView quizView) {
    log.info("Saving quiz with choices: {}", quizView);

    return quizViewRepository
        .insertQuizWithChoices(
            quizView.getCode(),
            quizView.getProgrammingLanguage().name(),
            quizView.getDifficultyLevel().name(),
            quizView.getCorrectAnswer().name(),
            quizView.getExplanation(),
            answerChoicesJsonMapper.toJson(quizView.getAnswerChoices()))
        .doOnSuccess(ignored -> log.info("Successfully saved quiz: {}", quizView.getCode()))
        .doOnError(error -> log.error("Failed to save quiz: {}", quizView.getCode(), error))
        .thenReturn(quizView);
  }
}
