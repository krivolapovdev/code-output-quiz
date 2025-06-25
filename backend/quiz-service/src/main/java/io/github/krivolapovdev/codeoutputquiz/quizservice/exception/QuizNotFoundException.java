package io.github.krivolapovdev.codeoutputquiz.quizservice.exception;

public class QuizNotFoundException extends RuntimeException {
  public QuizNotFoundException(String message) {
    super(message);
  }
}
