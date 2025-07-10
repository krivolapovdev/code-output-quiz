package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.config.cache.CacheNames;
import io.github.krivolapovdev.codeoutputquiz.quizservice.exception.QuizNotFoundException;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizRepository;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
  private final QuizRepository quizRepository;
  private final QuizAiService quizAiService;
  private final QuizMapper quizMapper;

  public Mono<QuizResponse> getRandomQuiz(QuizRequest quizRequest) {
    log.info(
        "Requesting random quiz for language: {}, level: {}",
        quizRequest.programmingLanguage(),
        quizRequest.difficultyLevel());
    return quizRepository
        .findRandomQuizView(quizRequest.programmingLanguage(), quizRequest.difficultyLevel())
        .doOnNext(quiz -> log.debug("Random quiz fetched from DB: {}", quiz))
        .map(quizMapper::toResponse);
  }

  @Cacheable(value = CacheNames.QUIZ_CACHE, key = "#id")
  public Mono<QuizResponse> getQuizById(UUID id) {
    log.info("Requesting quiz by id: {}", id);
    return quizRepository
        .findById(id)
        .doOnNext(quiz -> log.debug("Quiz fetched from DB: {}", quiz))
        .map(quizMapper::toResponse)
        .switchIfEmpty(
            Mono.defer(
                () -> Mono.error(new QuizNotFoundException("Quiz not found with id: " + id))));
  }

  public Flux<String> generateQuiz(QuizRequest quizRequest) {
    log.info("Generating quiz using AI for request: {}", quizRequest);
    return quizAiService
        .generateQuiz(quizRequest)
        .doOnNext(line -> log.debug("Generated quiz part: {}", line));
  }
}
