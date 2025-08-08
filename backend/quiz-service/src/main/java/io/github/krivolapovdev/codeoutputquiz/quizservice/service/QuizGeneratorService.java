package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import java.time.Duration;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        .flatMapIterable(
            lang ->
                Stream.of(DifficultyLevel.values())
                    .map(level -> new QuizRequest(lang, level))
                    .toList())
        .doOnNext(request -> log.info("Generating quiz for: {}", request))
        .concatMap(
            req ->
                quizAiService
                    .generateQuiz(req)
                    .delaySubscription(perQuizDelay)
                    .doOnError(
                        error -> log.warn("Skipping quiz {} due to generation error", req, error))
                    .onErrorResume(error -> Mono.empty()))
        .flatMap(
            quiz ->
                quizService
                    .saveQuizWithChoices(quiz)
                    .doOnError(
                        error -> log.warn("Failed to save quiz {}: {}", quiz, error.getMessage()))
                    .onErrorResume(error -> Mono.empty()))
        .then();
  }
}
