package io.github.krivolapovdev.codeoutputquiz.authservice.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private static final String LOG_OCCURRED_MESSAGE = "{} Occurred: {}";

  @ExceptionHandler(EmailAlreadyTakenException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Mono<ApiError> handleQuizNotFoundException(
      EmailAlreadyTakenException ex, ServerWebExchange exchange) {
    ApiError apiError = buildError(ex, ex.getMessage(), HttpStatus.CONFLICT, exchange);
    return Mono.just(apiError);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ApiError> handleValidationException(
      WebExchangeBindException ex, ServerWebExchange exchange) {
    String validationErrors =
        ex.getFieldErrors().stream()
            .map(error -> "`%s`: %s".formatted(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.joining(";"));
    ApiError apiError = buildError(ex, validationErrors, HttpStatus.BAD_REQUEST, exchange);
    return Mono.just(apiError);
  }

  private ApiError buildError(
      Throwable ex, String message, HttpStatus status, ServerWebExchange exchange) {
    log.warn(LOG_OCCURRED_MESSAGE, ex.getClass().getSimpleName(), message);
    String path = exchange.getRequest().getPath().value();
    int httpStatusCode = status.value();
    LocalDateTime now = LocalDateTime.now();
    return new ApiError(message, path, httpStatusCode, now);
  }
}
