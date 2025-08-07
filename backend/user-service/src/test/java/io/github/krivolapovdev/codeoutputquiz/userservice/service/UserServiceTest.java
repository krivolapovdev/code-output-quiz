package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.userservice.response.UserResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @InjectMocks private UserService userService;

  @Test
  void shouldReturnCurrentUser() {
    UUID userId = UUID.randomUUID();
    String email = "user@example.com";

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(email);

    AuthDetails authDetails = mock(AuthDetails.class);
    when(authDetails.userId()).thenReturn(userId);
    when(authentication.getDetails()).thenReturn(authDetails);

    userService
        .getCurrentUser(authentication)
        .as(StepVerifier::create)
        .expectNext(new UserResponse(userId, email))
        .verifyComplete();
  }
}
