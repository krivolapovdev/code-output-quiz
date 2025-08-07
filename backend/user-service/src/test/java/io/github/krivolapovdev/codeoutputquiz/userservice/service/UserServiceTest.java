package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
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

    AuthPrincipal authPrincipal = new AuthPrincipal(userId, email, UserRole.USER);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(authPrincipal);
    when(authentication.getName()).thenReturn(email); // опционально

    UserResponse expected = new UserResponse(userId, email);

    userService
        .getCurrentUser(authentication)
        .as(StepVerifier::create)
        .expectNext(expected)
        .verifyComplete();

    verify(authentication).getPrincipal();
  }
}
