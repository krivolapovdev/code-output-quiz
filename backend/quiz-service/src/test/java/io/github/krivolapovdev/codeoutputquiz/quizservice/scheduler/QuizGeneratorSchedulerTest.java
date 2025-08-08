package io.github.krivolapovdev.codeoutputquiz.quizservice.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizGeneratorService;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.scheduler.VirtualTimeScheduler;

@ExtendWith(MockitoExtension.class)
class QuizGeneratorSchedulerTest {
  @Mock private QuizGeneratorService quizGeneratorService;
  private VirtualTimeScheduler virtualTimeScheduler;
  private QuizGeneratorScheduler quizGeneratorScheduler;

  @BeforeEach
  void setUp() {
    virtualTimeScheduler = VirtualTimeScheduler.create();
    quizGeneratorScheduler = new QuizGeneratorScheduler(quizGeneratorService, virtualTimeScheduler);
  }

  @Test
  void shouldRunAfterInitialDelay_thenRepeatImmediatelyOnEachCompletion_andPassPerQuizDelay() {
    Duration initialDelay = Duration.ofMinutes(1);
    Duration perQuizDelay = Duration.ofMinutes(2);

    when(quizGeneratorService.generateNewQuizzes(any()))
        .thenAnswer(inv -> Mono.delay(Duration.ofMinutes(5), virtualTimeScheduler).then());

    quizGeneratorScheduler.start(initialDelay, perQuizDelay);

    verify(quizGeneratorService, never()).generateNewQuizzes(any());

    virtualTimeScheduler.advanceTimeBy(initialDelay);
    verify(quizGeneratorService, times(1)).generateNewQuizzes(any());

    virtualTimeScheduler.advanceTimeBy(Duration.ofMinutes(5));
    verify(quizGeneratorService, times(2)).generateNewQuizzes(any());

    virtualTimeScheduler.advanceTimeBy(Duration.ofMinutes(5));
    verify(quizGeneratorService, times(3)).generateNewQuizzes(any());

    ArgumentCaptor<Duration> arg = ArgumentCaptor.forClass(Duration.class);
    verify(quizGeneratorService, atLeastOnce()).generateNewQuizzes(arg.capture());
    assertThat(arg.getAllValues()).allMatch(perQuizDelay::equals);
  }

  @Test
  void shouldContinueSchedulingWhenRunErrors() {
    Duration initialDelay = Duration.ofMinutes(1);

    when(quizGeneratorService.generateNewQuizzes(any()))
        .thenAnswer(
            inv ->
                Mono.delay(Duration.ofMillis(1), virtualTimeScheduler)
                    .then(Mono.error(new RuntimeException("boom"))))
        .thenAnswer(inv -> Mono.delay(Duration.ofMinutes(5), virtualTimeScheduler).then())
        .thenAnswer(inv -> Mono.delay(Duration.ofMinutes(5), virtualTimeScheduler).then());

    quizGeneratorScheduler.start(initialDelay, Duration.ofMinutes(2));

    virtualTimeScheduler.advanceTimeBy(initialDelay);
    verify(quizGeneratorService, times(1)).generateNewQuizzes(any());

    virtualTimeScheduler.advanceTimeBy(Duration.ofMillis(1));
    verify(quizGeneratorService, times(2)).generateNewQuizzes(any());

    virtualTimeScheduler.advanceTimeBy(Duration.ofMinutes(5));
    verify(quizGeneratorService, times(3)).generateNewQuizzes(any());
  }

  @Test
  void shouldStopSchedulingOnDispose() {
    Duration initialDelay = Duration.ofMinutes(1);

    when(quizGeneratorService.generateNewQuizzes(any()))
        .thenAnswer(inv -> Mono.delay(Duration.ofMinutes(5), virtualTimeScheduler).then());

    quizGeneratorScheduler.start(initialDelay, Duration.ofMinutes(2));

    virtualTimeScheduler.advanceTimeBy(initialDelay);
    verify(quizGeneratorService, times(1)).generateNewQuizzes(any());

    quizGeneratorScheduler.stop();

    virtualTimeScheduler.advanceTimeBy(Duration.ofMinutes(60));
    verify(quizGeneratorService, times(1)).generateNewQuizzes(any());
  }
}
