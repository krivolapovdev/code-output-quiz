package io.github.nellshark.codeoutputquiz.authservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private static final String LOG_OCCURRED_MESSAGE = "{} Occurred: {}";

  @ExceptionHandler(EmailAlreadyTakenException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Mono<String> handleQuizNotFoundException(EmailAlreadyTakenException ex) {
    log.warn(LOG_OCCURRED_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());
    return Mono.just(ex.getMessage());
  }
}
