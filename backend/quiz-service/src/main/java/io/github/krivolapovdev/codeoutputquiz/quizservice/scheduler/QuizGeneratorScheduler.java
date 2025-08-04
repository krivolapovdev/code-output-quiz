package io.github.krivolapovdev.codeoutputquiz.quizservice.scheduler;

import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizGeneratorService;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
class QuizGeneratorScheduler {
  private final QuizGeneratorService quizGeneratorService;

  @PostConstruct
  public void schedule() {
    Mono.delay(Duration.ofMinutes(1))
        .then(
            Mono.defer(
                () ->
                    quizGeneratorService
                        .generateNewQuizzes()
                        .doOnSuccess(v -> log.info("Finished generating quizzes"))
                        .delayElement(Duration.ofMinutes(10))))
        .repeat()
        .subscribe();
  }
}
