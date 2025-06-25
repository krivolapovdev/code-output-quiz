package io.github.krivolapovdev.codeoutputquiz.quizservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(QuizNotFoundException.class)
  public Mono<ResponseEntity<String>> handleQuizNotFoundException(QuizNotFoundException ex) {
    log.warn("Quiz not found: {}", ex.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
  }
}
