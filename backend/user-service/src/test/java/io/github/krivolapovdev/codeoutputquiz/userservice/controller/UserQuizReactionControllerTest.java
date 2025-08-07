package io.github.krivolapovdev.codeoutputquiz.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.userservice.TestSecurityConfig;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserQuizReactionRequest;
import io.github.krivolapovdev.codeoutputquiz.userservice.service.UserQuizReactionService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserQuizReactionController.class)
@Import(TestSecurityConfig.class)
class UserQuizReactionControllerTest {
  @Autowired private WebTestClient webTestClient;
  @MockitoBean private UserQuizReactionService userQuizReactionService;

  @Test
  void shouldReactToQuizSuccessfully() {
    var request = new UserQuizReactionRequest(UUID.randomUUID(), true);

    when(userQuizReactionService.reactToQuiz(eq(request), any())).thenReturn(Mono.empty());

    webTestClient
        .post()
        .uri("/api/v1/users/me/quiz-reactions")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();

    verify(userQuizReactionService).reactToQuiz(eq(request), any());
  }
}
