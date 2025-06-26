package io.github.nellshark.codeoutputquiz.authservice.exception;

public class EmailAlreadyTakenException extends RuntimeException {
  public EmailAlreadyTakenException(String message) {
    super(message);
  }
}
