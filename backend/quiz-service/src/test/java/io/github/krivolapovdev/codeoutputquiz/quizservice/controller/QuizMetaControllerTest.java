package io.github.krivolapovdev.codeoutputquiz.quizservice.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.quizservice.config.SecurityConfig;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.ProgrammingLanguageResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.security.jwt.JwtTokenProvider;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizMetaService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = QuizMetaController.class)
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
class QuizMetaControllerTest {
  @Autowired private WebTestClient webTestClient;

  @MockitoBean private JwtTokenProvider jwtTokenProvider;

  @MockitoBean private QuizMetaService quizMetaService;

  @Test
  void shouldReturnSupportedProgrammingLanguages() {
    List<ProgrammingLanguageResponse> mockResponse =
        List.of(new ProgrammingLanguageResponse("JAVA", "Java"));

    when(quizMetaService.getSupportedProgrammingLanguages()).thenReturn(Mono.just(mockResponse));

    webTestClient
        .get()
        .uri("/api/v1/quizzes/meta/supported-programming-languages")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(ProgrammingLanguageResponse.class)
        .isEqualTo(mockResponse);

    verify(quizMetaService).getSupportedProgrammingLanguages();
  }

  @Test
  void shouldReturnSupportedDifficultyLevels() {
    List<DifficultyLevel> mockResponse =
        List.of(DifficultyLevel.BEGINNER, DifficultyLevel.ADVANCED);

    when(quizMetaService.getSupportedDifficultyLevels()).thenReturn(Mono.just(mockResponse));

    webTestClient
        .get()
        .uri("/api/v1/quizzes/meta/supported-difficulty-levels")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(DifficultyLevel.class)
        .isEqualTo(mockResponse);

    verify(quizMetaService).getSupportedDifficultyLevels();
  }
}
