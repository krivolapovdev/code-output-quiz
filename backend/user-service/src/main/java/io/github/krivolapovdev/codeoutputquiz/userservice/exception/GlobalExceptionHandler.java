package io.github.krivolapovdev.codeoutputquiz.userservice.exception;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ResponseStatusException> handleValidationException(WebExchangeBindException ex) {
    String validationErrors =
        ex.getFieldErrors().stream()
            .map(error -> "`%s`: %s".formatted(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.joining(";"));

    return buildMonoResponseStatusException(HttpStatus.BAD_REQUEST, validationErrors, ex);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Mono<ResponseStatusException> handleAuthorizationDeniedException(
      AuthorizationDeniedException ex) {
    return buildMonoResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage(), ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<ResponseStatusException> handleGeneralException(Exception ex) {
    return buildMonoResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
  }

  private Mono<ResponseStatusException> buildMonoResponseStatusException(
      HttpStatusCode status, String message, Throwable cause) {
    log.warn("{} Occurred: {}", cause.getClass().getSimpleName(), message);
    ResponseStatusException responseStatusException =
        new ResponseStatusException(status, message, cause);
    return Mono.just(responseStatusException);
  }
}
