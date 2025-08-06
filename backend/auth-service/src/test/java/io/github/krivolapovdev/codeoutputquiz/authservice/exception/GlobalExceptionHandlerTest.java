package io.github.krivolapovdev.codeoutputquiz.authservice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  void shouldReturnConflictForEmailAlreadyTakenException() {
    var ex = new EmailAlreadyTakenException("Email already exists");

    globalExceptionHandler
        .handleQuizNotFoundException(ex)
        .as(StepVerifier::create)
        .assertNext(
            err -> {
              assertThat(err).isInstanceOf(ResponseStatusException.class);
              assertThat(err.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
              assertThat(err.getReason()).isEqualTo("Email already exists");
            })
        .verifyComplete();
  }

  @Test
  void shouldReturnBadRequestForWebExchangeBindException() {
    var target = new Object();
    var bindException = new BindException(target, "authRequest");
    bindException.addError(new FieldError("authRequest", "email", "must not be blank"));
    bindException.addError(new FieldError("authRequest", "password", "must not be null"));

    var webExchangeBindException = new WebExchangeBindException(null, bindException);

    globalExceptionHandler
        .handleValidationException(webExchangeBindException)
        .as(StepVerifier::create)
        .assertNext(
            err -> {
              assertThat(err).isInstanceOf(ResponseStatusException.class);
              assertThat(err.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(err.getReason())
                  .contains("`email`: must not be blank")
                  .contains("`password`: must not be null");
            })
        .verifyComplete();
  }

  @Test
  void shouldReturnForbiddenForAuthorizationDeniedException() {
    var ex = new AuthorizationDeniedException("Access denied");

    globalExceptionHandler
        .handleAuthorizationDeniedException(ex)
        .as(StepVerifier::create)
        .assertNext(
            err -> {
              assertThat(err).isInstanceOf(ResponseStatusException.class);
              assertThat(err.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
              assertThat(err.getReason()).isEqualTo("Access denied");
            })
        .verifyComplete();
  }

  @Test
  void shouldReturnUnauthorizedForJwtException() {
    var ex = new JwtException("Invalid JWT token");

    globalExceptionHandler
        .handleJwtException(ex)
        .as(StepVerifier::create)
        .assertNext(
            err -> {
              assertThat(err).isInstanceOf(ResponseStatusException.class);
              assertThat(err.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
              assertThat(err.getReason()).isEqualTo("Invalid JWT token");
            })
        .verifyComplete();
  }

  @Test
  void shouldReturnNotFoundForUsernameNotFoundException() {
    var ex = new UsernameNotFoundException("User not found");

    globalExceptionHandler
        .handleUsernameNotFoundException(ex)
        .as(StepVerifier::create)
        .assertNext(
            err -> {
              assertThat(err).isInstanceOf(ResponseStatusException.class);
              assertThat(err.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
              assertThat(err.getReason()).isEqualTo("User not found");
            })
        .verifyComplete();
  }
}
