package io.github.krivolapovdev.codeoutputquiz.quizservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(QuizNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Mono<ResponseStatusException> handleQuizNotFoundException(QuizNotFoundException ex) {
    return buildMonoResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
  }

  private Mono<ResponseStatusException> buildMonoResponseStatusException(
      HttpStatusCode status, String message, Throwable cause) {
    log.warn("{} Occurred: {}", cause.getClass().getSimpleName(), message);
    ResponseStatusException responseStatusException =
        new ResponseStatusException(status, message, cause);
    return Mono.just(responseStatusException);
  }
}
