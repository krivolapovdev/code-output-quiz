package io.github.krivolapovdev.codeoutputquiz.userservice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  @InjectMocks private GlobalExceptionHandler exceptionHandler;

  @Test
  void shouldHandleValidationException() {
    BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
    bindingResult.addError(new FieldError("target", "email", "must not be blank"));
    bindingResult.addError(new FieldError("target", "password", "must be at least 6 characters"));

    @SuppressWarnings("DataFlowIssue")
    WebExchangeBindException exception = new WebExchangeBindException(null, bindingResult);

    exceptionHandler
        .handleValidationException(exception)
        .as(StepVerifier::create)
        .assertNext(
            response -> {
              assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(response.getReason()).contains("`email`: must not be blank");
              assertThat(response.getReason())
                  .contains("`password`: must be at least 6 characters");
              assertThat(response.getCause()).isSameAs(exception);
            })
        .verifyComplete();
  }

  @Test
  void shouldHandleAuthorizationDeniedException() {
    AuthorizationDeniedException ex = new AuthorizationDeniedException("Forbidden action");

    exceptionHandler
        .handleAuthorizationDeniedException(ex)
        .as(StepVerifier::create)
        .assertNext(
            response -> {
              assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
              assertThat(response.getReason()).isEqualTo("Forbidden action");
              assertThat(response.getCause()).isSameAs(ex);
            })
        .verifyComplete();
  }

  @Test
  void shouldHandleGeneralException() {
    Exception ex = new RuntimeException("Unexpected failure");

    exceptionHandler
        .handleGeneralException(ex)
        .as(StepVerifier::create)
        .assertNext(
            response -> {
              assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
              assertThat(response.getReason()).isEqualTo("Unexpected failure");
              assertThat(response.getCause()).isSameAs(ex);
            })
        .verifyComplete();
  }
}
