package io.github.krivolapovdev.codeoutputquiz.quizservice.scheduler;

import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizGeneratorService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
@RequiredArgsConstructor
@Slf4j
class QuizGeneratorScheduler {
  private final QuizGeneratorService quizGeneratorService;
  private final Scheduler quizClock;
  private Disposable subscription;

  @PostConstruct
  public void schedule() {
    start(Duration.ofMinutes(1), Duration.ofMinutes(2));
  }

  @PreDestroy
  public void stop() {
    if (subscription != null) subscription.dispose();
  }

  void start(@NonNull Duration initialDelay, @NonNull Duration perQuizDelay) {
    subscription =
        Mono.delay(initialDelay, quizClock)
            .thenMany(
                Mono.defer(
                        () ->
                            quizGeneratorService
                                .generateNewQuizzes(perQuizDelay)
                                .doOnSuccess(v -> log.info("Finished generating quizzes"))
                                .doOnError(
                                    e -> log.warn("Quiz generation failed, continuing schedule", e))
                                .onErrorResume(e -> Mono.empty()))
                    .repeat())
            .subscribe();
  }
}
