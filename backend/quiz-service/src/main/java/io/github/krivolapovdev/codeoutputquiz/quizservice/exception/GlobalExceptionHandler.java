package io.github.krivolapovdev.codeoutputquiz.quizservice.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private static final String LOG_OCCURRED_MESSAGE = "{} Occurred: {}";

  @ExceptionHandler(QuizNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Mono<ApiError> handleQuizNotFoundException(
      QuizNotFoundException ex, ServerWebExchange exchange) {
    log.warn(LOG_OCCURRED_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());
    return Mono.just(buildError(ex, HttpStatus.CONFLICT, exchange));
  }

  private ApiError buildError(Throwable ex, HttpStatus status, ServerWebExchange exchange) {
    return new ApiError(
        ex.getMessage(),
        exchange.getRequest().getPath().value(),
        status.value(),
        LocalDateTime.now());
  }
}
