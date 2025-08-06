package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
  @Mock private UserService userService;
  @InjectMocks private CustomUserDetailsService customUserDetailsService;

  @Test
  void shouldFindUserByUsername() {
    String email = "TEST@example.com";
    String normalizedEmail = "test@example.com";
    String password = "encoded-password";
    UserRole role = UserRole.USER;

    var appUser = new User(normalizedEmail, password);
    appUser.setRole(role);

    when(userService.getUserByEmail(normalizedEmail)).thenReturn(Mono.just(appUser));

    customUserDetailsService
        .findByUsername(email)
        .as(StepVerifier::create)
        .assertNext(
            userDetails -> {
              assertThat(userDetails.getUsername()).isEqualTo(normalizedEmail);
              assertThat(userDetails.getPassword()).isEqualTo(password);
              assertThat(userDetails.getAuthorities())
                  .extracting(Object::toString)
                  .containsExactly("ROLE_" + role.name());
            })
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyWhenUserNotFound() {
    String email = "unknown@example.com";
    String normalizedEmail = email.toLowerCase();

    when(userService.getUserByEmail(normalizedEmail)).thenReturn(Mono.empty());

    customUserDetailsService.findByUsername(email).as(StepVerifier::create).verifyComplete();
  }
}
