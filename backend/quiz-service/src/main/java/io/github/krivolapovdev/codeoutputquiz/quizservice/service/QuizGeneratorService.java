package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizGeneratorService {

  private final QuizAiService quizAiService;
  private final QuizService quizService;

  public Mono<Void> generateNewQuizzes(@NonNull Duration perQuizDelay) {
    log.info("Starting quiz generation for all languages and difficulty levels");

    return Flux.fromArray(ProgrammingLanguage.values())
        .concatMap(
            lang ->
                Flux.fromArray(DifficultyLevel.values()).map(level -> new QuizRequest(lang, level)))
        .concatMap(
            request ->
                quizAiService
                    .generateQuiz(request)
                    .flatMap(quizService::saveQuizWithChoices)
                    .doOnSuccess(v -> log.info("Saved quiz for: {}", request))
                    .onErrorResume(
                        DuplicateKeyException.class,
                        e -> {
                          log.warn("Duplicate quiz for {} — skipping.", request);
                          return Mono.empty();
                        })
                    .onErrorResume(
                        e -> {
                          log.warn("Failed to generate/save for {} — skipping.", request, e);
                          return Mono.empty();
                        })
                    .then()
                    .delayElement(perQuizDelay))
        .then();
  }
}
