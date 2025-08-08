package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import static org.mockito.Mockito.*;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class QuizGeneratorServiceTest {
  @Mock private QuizAiService quizAiService;
  @Mock private QuizService quizService;
  @InjectMocks private QuizGeneratorService quizGeneratorService;

  private static final Duration DELAY = Duration.ZERO;

  @Test
  void shouldGenerateAndSaveAllQuizzes() {
    Stream.of(ProgrammingLanguage.values())
        .flatMap(
            lang -> Stream.of(DifficultyLevel.values()).map(level -> new QuizRequest(lang, level)))
        .forEach(
            req -> {
              QuizView quiz = mock(QuizView.class);
              when(quizAiService.generateQuiz(req)).thenReturn(Mono.just(quiz));
              when(quizService.saveQuizWithChoices(quiz)).thenReturn(Mono.empty());
            });

    quizGeneratorService.generateNewQuizzes(DELAY).as(StepVerifier::create).verifyComplete();

    int expectedCount = ProgrammingLanguage.values().length * DifficultyLevel.values().length;
    verify(quizAiService, times(expectedCount)).generateQuiz(any());
    verify(quizService, times(expectedCount)).saveQuizWithChoices(any());
    verifyNoMoreInteractions(quizAiService, quizService);
  }

  @Test
  void shouldSkipWhenQuizGenerationFails() {
    ProgrammingLanguage failLang = ProgrammingLanguage.JAVA;
    DifficultyLevel failLevel = DifficultyLevel.BEGINNER;
    QuizRequest failReq = new QuizRequest(failLang, failLevel);

    when(quizAiService.generateQuiz(failReq))
        .thenReturn(Mono.error(new RuntimeException("AI fail")));

    Stream.of(ProgrammingLanguage.values())
        .flatMap(
            lang -> Stream.of(DifficultyLevel.values()).map(level -> new QuizRequest(lang, level)))
        .filter(req -> !req.equals(failReq))
        .forEach(
            req -> {
              QuizView quiz = mock(QuizView.class);
              when(quizAiService.generateQuiz(req)).thenReturn(Mono.just(quiz));
              when(quizService.saveQuizWithChoices(quiz)).thenReturn(Mono.empty());
            });

    quizGeneratorService.generateNewQuizzes(DELAY).as(StepVerifier::create).verifyComplete();

    verify(quizAiService, atLeastOnce()).generateQuiz(any());
    verify(quizService, atLeastOnce()).saveQuizWithChoices(any());
  }

  @Test
  void shouldSkipWhenSavingQuizFails() {
    ProgrammingLanguage failLang = ProgrammingLanguage.PYTHON;
    DifficultyLevel failLevel = DifficultyLevel.ADVANCED;
    QuizRequest failReq = new QuizRequest(failLang, failLevel);
    QuizView failQuiz = mock(QuizView.class);

    when(quizAiService.generateQuiz(failReq)).thenReturn(Mono.just(failQuiz));
    when(quizService.saveQuizWithChoices(failQuiz))
        .thenReturn(Mono.error(new RuntimeException("DB fail")));

    Stream.of(ProgrammingLanguage.values())
        .flatMap(
            lang -> Stream.of(DifficultyLevel.values()).map(level -> new QuizRequest(lang, level)))
        .filter(req -> !req.equals(failReq))
        .forEach(
            req -> {
              QuizView quiz = mock(QuizView.class);
              when(quizAiService.generateQuiz(req)).thenReturn(Mono.just(quiz));
              when(quizService.saveQuizWithChoices(quiz)).thenReturn(Mono.empty());
            });

    quizGeneratorService.generateNewQuizzes(DELAY).as(StepVerifier::create).verifyComplete();

    verify(quizAiService, atLeastOnce()).generateQuiz(any());
    verify(quizService, atLeastOnce()).saveQuizWithChoices(any());
  }
}
