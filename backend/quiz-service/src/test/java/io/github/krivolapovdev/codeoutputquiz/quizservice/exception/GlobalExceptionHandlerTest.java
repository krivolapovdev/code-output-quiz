package io.github.krivolapovdev.codeoutputquiz.quizservice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  void shouldReturnNotFoundForQuizNotFoundException() {
    String message = "Quiz with given ID not found";
    QuizNotFoundException ex = new QuizNotFoundException(message);

    globalExceptionHandler
        .handleQuizNotFoundException(ex)
        .as(StepVerifier::create)
        .assertNext(
            error -> {
              assertThat(error).isInstanceOf(ResponseStatusException.class);
              assertThat(error.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
              assertThat(error.getReason()).isEqualTo(message);
              assertThat(error.getCause()).isEqualTo(ex);
            })
        .verifyComplete();
  }
}
