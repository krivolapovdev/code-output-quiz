package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.userservice.TestSecurityConfig;
import io.github.krivolapovdev.codeoutputquiz.userservice.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserSolvedQuizRequest;
import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserSolvedQuizService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserSolvedQuizController.class)
@Import(TestSecurityConfig.class)
class UserSolvedQuizControllerTest {
  @Autowired private WebTestClient webTestClient;
  @MockitoBean private UserSolvedQuizService userSolvedQuizService;

  @Test
  void shouldAddUserSolvedQuiz() {
    var request = new UserSolvedQuizRequest(UUID.randomUUID(), AnswerChoice.A);

    when(userSolvedQuizService.addUserSolvedQuiz(eq(request), any())).thenReturn(Mono.empty());

    webTestClient
        .post()
        .uri("/api/v1/users/me/solved-quizzes")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();

    verify(userSolvedQuizService).addUserSolvedQuiz(eq(request), any());
  }
}
