package io.github.krivolapovdev.codeoutputquiz.quizservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizService;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = QuizController.class)
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class QuizControllerTest {
  @Autowired private WebTestClient webTestClient;
  @MockitoBean private QuizService quizService;

  @Test
  void shouldReturnQuizById() {
    UUID quizId = UUID.randomUUID();

    List<AnswerChoiceData> answerChoices =
        List.of(
            new AnswerChoiceData(AnswerChoice.A, "int x = 5;"),
            new AnswerChoiceData(AnswerChoice.B, "String x = '5';"),
            new AnswerChoiceData(AnswerChoice.C, "float x = 5.0f;"),
            new AnswerChoiceData(AnswerChoice.D, "boolean x = true;"));

    QuizResponse expected =
        new QuizResponse(
            quizId,
            "What is the correct way to declare an integer in Java?",
            AnswerChoice.A,
            "In Java, `int` is used for integer declaration. Option A is correct.",
            ProgrammingLanguage.JAVA,
            DifficultyLevel.BEGINNER,
            answerChoices);

    when(quizService.getQuizById(quizId)).thenReturn(Mono.just(expected));

    webTestClient
        .get()
        .uri("/api/v1/quizzes/{id}", quizId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(QuizResponse.class)
        .value(response -> assertThat(response).isEqualTo(expected));

    verify(quizService).getQuizById(quizId);
  }

  @Test
  void shouldReturnRandomQuiz() {
    ProgrammingLanguage language = ProgrammingLanguage.JAVA;
    DifficultyLevel level = DifficultyLevel.BEGINNER;
    List<AnswerChoiceData> pythonChoices =
        List.of(
            new AnswerChoiceData(AnswerChoice.A, "def func(): pass"),
            new AnswerChoiceData(AnswerChoice.B, "function func() {}"),
            new AnswerChoiceData(AnswerChoice.C, "func := function() {}"),
            new AnswerChoiceData(AnswerChoice.D, "def func[] -> pass"));
    QuizResponse expected =
        new QuizResponse(
            UUID.randomUUID(),
            "Which option defines a function correctly in Python?",
            AnswerChoice.A,
            "In Python, functions are defined using `def`. Option A is correct.",
            ProgrammingLanguage.PYTHON,
            DifficultyLevel.INTERMEDIATE,
            pythonChoices);

    when(quizService.getRandomQuiz(language, level)).thenReturn(Mono.just(expected));

    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/api/v1/quizzes/random")
                    .queryParam("programmingLanguage", language)
                    .queryParam("difficultyLevel", level)
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(QuizResponse.class)
        .value(response -> assertThat(response).isEqualTo(expected));

    verify(quizService).getRandomQuiz(language, level);
  }

  @Test
  void shouldReturnUserUnsolvedQuiz() {
    ProgrammingLanguage language = ProgrammingLanguage.JAVA;
    DifficultyLevel level = DifficultyLevel.BEGINNER;
    List<AnswerChoiceData> goChoices =
        List.of(
            new AnswerChoiceData(AnswerChoice.A, "var x int = 10"),
            new AnswerChoiceData(AnswerChoice.B, "x := 10"),
            new AnswerChoiceData(AnswerChoice.C, "int x = 10"),
            new AnswerChoiceData(AnswerChoice.D, "define x as int = 10"));
    QuizResponse expected =
        new QuizResponse(
            UUID.randomUUID(),
            "Which Go statement correctly declares and initializes a variable?",
            AnswerChoice.B,
            "In Go, the short variable declaration `:=` is used. Option B is correct.",
            ProgrammingLanguage.GO,
            DifficultyLevel.ADVANCED,
            goChoices);

    when(quizService.getUserUnsolvedQuiz(eq(language), eq(level), any()))
        .thenReturn(Mono.just(expected));

    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/api/v1/quizzes/unsolved")
                    .queryParam("programmingLanguage", language)
                    .queryParam("difficultyLevel", level)
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(QuizResponse.class)
        .value(response -> assertThat(response).isEqualTo(expected));

    verify(quizService).getUserUnsolvedQuiz(eq(language), eq(level), any());
  }
}
