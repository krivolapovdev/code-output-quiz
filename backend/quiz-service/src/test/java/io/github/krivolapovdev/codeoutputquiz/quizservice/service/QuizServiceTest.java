package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.exception.QuizNotFoundException;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.AnswerChoicesJsonMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizViewRepository;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {
  @Mock private QuizViewRepository quizViewRepository;
  @Mock private QuizMapper quizMapper;
  @Mock private AnswerChoicesJsonMapper answerChoicesJsonMapper;
  @InjectMocks private QuizService quizService;

  @Test
  void shouldReturnRandomQuizMapped() {
    ProgrammingLanguage lang = ProgrammingLanguage.JAVA;
    DifficultyLevel level = DifficultyLevel.BEGINNER;

    QuizView view = mock(QuizView.class);
    QuizResponse response = mock(QuizResponse.class);

    when(quizViewRepository.findRandomQuizView(lang, level)).thenReturn(Mono.just(view));
    when(quizMapper.toResponse(view)).thenReturn(response);

    quizService
        .getRandomQuiz(lang, level)
        .as(StepVerifier::create)
        .assertNext(actual -> assertThat(actual).isSameAs(response))
        .verifyComplete();

    verify(quizViewRepository, only()).findRandomQuizView(lang, level);
    verify(quizMapper).toResponse(view);
  }

  @Test
  void shouldErrorWhenRandomQuizNotFound() {
    ProgrammingLanguage lang = ProgrammingLanguage.JAVA;
    DifficultyLevel level = DifficultyLevel.BEGINNER;

    when(quizViewRepository.findRandomQuizView(lang, level)).thenReturn(Mono.empty());

    quizService
        .getRandomQuiz(lang, level)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            ex ->
                assertThat(ex)
                    .isInstanceOf(QuizNotFoundException.class)
                    .hasMessageContaining("Random Quiz not found"))
        .verify();

    verify(quizViewRepository, only()).findRandomQuizView(lang, level);
    verifyNoInteractions(quizMapper);
  }

  @Test
  void shouldReturnQuizByIdMapped() {
    UUID id = UUID.randomUUID();
    QuizView view = mock(QuizView.class);
    QuizResponse response = mock(QuizResponse.class);

    when(quizViewRepository.findById(id)).thenReturn(Mono.just(view));
    when(quizMapper.toResponse(view)).thenReturn(response);

    quizService
        .getQuizById(id)
        .as(StepVerifier::create)
        .assertNext(actual -> assertThat(actual).isSameAs(response))
        .verifyComplete();

    verify(quizViewRepository, only()).findById(id);
    verify(quizMapper).toResponse(view);
  }

  @Test
  void shouldErrorWhenQuizByIdNotFound() {
    UUID id = UUID.randomUUID();
    when(quizViewRepository.findById(id)).thenReturn(Mono.empty());

    quizService
        .getQuizById(id)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            ex ->
                assertThat(ex)
                    .isInstanceOf(QuizNotFoundException.class)
                    .hasMessageContaining("Quiz not found with id: " + id))
        .verify();

    verify(quizViewRepository, only()).findById(id);
    verifyNoInteractions(quizMapper);
  }

  @Test
  void shouldReturnUserUnsolvedQuizMapped() {
    ProgrammingLanguage lang = ProgrammingLanguage.PYTHON;
    DifficultyLevel level = DifficultyLevel.INTERMEDIATE;

    UUID userId = UUID.randomUUID();
    AuthPrincipal principal = mock(AuthPrincipal.class);
    when(principal.id()).thenReturn(userId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(principal);

    QuizView view = mock(QuizView.class);
    QuizResponse response = mock(QuizResponse.class);

    when(quizViewRepository.findUserUnsolvedQuiz(lang, level, userId)).thenReturn(Mono.just(view));
    when(quizMapper.toResponse(view)).thenReturn(response);

    quizService
        .getUserUnsolvedQuiz(lang, level, authentication)
        .as(StepVerifier::create)
        .assertNext(actual -> assertThat(actual).isSameAs(response))
        .verifyComplete();

    verify(quizViewRepository, only()).findUserUnsolvedQuiz(lang, level, userId);
    verify(quizMapper).toResponse(view);
  }

  @Test
  void shouldErrorWhenUserUnsolvedQuizNotFound() {
    ProgrammingLanguage lang = ProgrammingLanguage.PYTHON;
    DifficultyLevel level = DifficultyLevel.INTERMEDIATE;

    UUID userId = UUID.randomUUID();
    AuthPrincipal principal = mock(AuthPrincipal.class);
    when(principal.id()).thenReturn(userId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(principal);

    when(quizViewRepository.findUserUnsolvedQuiz(lang, level, userId)).thenReturn(Mono.empty());

    quizService
        .getUserUnsolvedQuiz(lang, level, authentication)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            ex ->
                assertThat(ex)
                    .isInstanceOf(QuizNotFoundException.class)
                    .hasMessageContaining("Quiz not found for user: " + userId))
        .verify();

    verify(quizViewRepository, only()).findUserUnsolvedQuiz(lang, level, userId);
    verifyNoInteractions(quizMapper);
  }

  @Test
  void shouldInsertQuizWithChoicesAndReturnOriginal() {
    QuizView view = mock(QuizView.class);
    when(view.getCode()).thenReturn("System.out.println(42);");
    when(view.getProgrammingLanguage()).thenReturn(ProgrammingLanguage.JAVA);
    when(view.getDifficultyLevel()).thenReturn(DifficultyLevel.BEGINNER);
    when(view.getCorrectAnswer()).thenReturn(AnswerChoice.A);
    when(view.getExplanation()).thenReturn("Because 42.");
    when(view.getAnswerChoices()).thenReturn(List.of());

    when(answerChoicesJsonMapper.toJson(List.of())).thenReturn("[]");

    when(quizViewRepository.insertQuizWithChoices(
            "System.out.println(42);", "JAVA", "BEGINNER", "A", "Because 42.", "[]"))
        .thenReturn(Mono.empty());

    quizService
        .saveQuizWithChoices(view)
        .as(StepVerifier::create)
        .assertNext(actual -> assertThat(actual).isSameAs(view))
        .verifyComplete();

    verify(quizViewRepository, only())
        .insertQuizWithChoices(
            "System.out.println(42);", "JAVA", "BEGINNER", "A", "Because 42.", "[]");
  }

  @Test
  void shouldPropagateErrorWhenInsertFails() {
    QuizView view = mock(QuizView.class);
    when(view.getCode()).thenReturn("code");
    when(view.getProgrammingLanguage()).thenReturn(ProgrammingLanguage.JAVA);
    when(view.getDifficultyLevel()).thenReturn(DifficultyLevel.BEGINNER);
    when(view.getCorrectAnswer()).thenReturn(AnswerChoice.B);
    when(view.getExplanation()).thenReturn("exp");
    when(view.getAnswerChoices()).thenReturn(List.of());

    when(answerChoicesJsonMapper.toJson(List.of())).thenReturn("[]");

    RuntimeException boom = new RuntimeException("db fail");
    when(quizViewRepository.insertQuizWithChoices("code", "JAVA", "BEGINNER", "B", "exp", "[]"))
        .thenReturn(Mono.error(boom));

    quizService
        .saveQuizWithChoices(view)
        .as(StepVerifier::create)
        .expectErrorSatisfies(ex -> assertThat(ex).isSameAs(boom))
        .verify();

    verify(quizViewRepository, only())
        .insertQuizWithChoices("code", "JAVA", "BEGINNER", "B", "exp", "[]");
  }
}
