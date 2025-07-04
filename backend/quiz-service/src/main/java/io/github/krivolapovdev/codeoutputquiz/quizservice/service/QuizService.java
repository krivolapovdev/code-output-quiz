package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.config.cache.CacheNames;
import io.github.krivolapovdev.codeoutputquiz.quizservice.exception.QuizNotFoundException;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizViewMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizViewRepository;
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
  private final QuizViewRepository quizViewRepository;
  private final QuizAiService quizAiService;
  private final QuizViewMapper quizViewMapper;

  public Mono<QuizResponse> getRandomQuiz(QuizRequest quizRequest) {
    log.info("Get random quiz {}", quizRequest);
    return quizViewRepository
        .findRandomQuizView(quizRequest.programmingLanguage(), quizRequest.difficultyLevel())
        .doOnNext(quiz -> log.info("Fetched quiz from DB: {}", quiz))
        .map(quizViewMapper::toResponse);
  }

  @Cacheable(value = CacheNames.QUIZ_CACHE, key = "#id")
  public Mono<QuizResponse> getQuizById(UUID id) {
    log.info("Get quiz by id {}", id);
    return quizViewRepository
        .findById(id)
        .map(quizViewMapper::toResponse)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Quiz not found with id: " + id)));
  }

  public Flux<String> generateQuiz(QuizRequest quizRequest) {
    return quizAiService.generateQuiz(quizRequest);
  }
}
