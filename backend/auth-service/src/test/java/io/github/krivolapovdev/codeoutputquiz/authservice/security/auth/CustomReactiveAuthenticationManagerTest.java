package io.github.krivolapovdev.codeoutputquiz.authservice.security.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.UserService;
import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CustomReactiveAuthenticationManagerTest {
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserService userService;
  @InjectMocks private CustomReactiveAuthenticationManager customReactiveAuthenticationManager;

  @Test
  void shouldAuthenticateSuccessfully() {
    String email = "user@example.com";
    String rawPassword = "password";
    String hashedPassword = "hashed";
    UUID userId = UUID.randomUUID();
    UserRole role = UserRole.USER;

    var user = new User(email, hashedPassword);
    user.setId(userId);
    user.setRole(role);

    var authentication = new UsernamePasswordAuthenticationToken(email, rawPassword);

    when(userService.getUserByEmail(email)).thenReturn(Mono.just(user));
    when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

    customReactiveAuthenticationManager
        .authenticate(authentication)
        .as(StepVerifier::create)
        .assertNext(
            auth -> {
              assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);

              var principal = (AuthPrincipal) auth.getPrincipal();
              assertThat(principal.email()).isEqualTo(email);
              assertThat(principal.id()).isEqualTo(userId);
              assertThat(principal.role()).isEqualTo(role);

              assertThat(auth.getAuthorities())
                  .extracting("authority")
                  .containsExactly(role.name());
            })
        .verifyComplete();

    verify(userService).getUserByEmail(email);
    verify(passwordEncoder).matches(rawPassword, hashedPassword);
  }

  @Test
  void shouldFailWhenUserNotFound() {
    String email = "notfound@example.com";
    String password = "irrelevant";
    var authentication = new UsernamePasswordAuthenticationToken(email, password);

    when(userService.getUserByEmail(email))
        .thenReturn(Mono.error(new UsernameNotFoundException("User not found")));

    customReactiveAuthenticationManager
        .authenticate(authentication)
        .as(StepVerifier::create)
        .expectErrorMatches(
            error ->
                error instanceof UsernameNotFoundException
                    && error.getMessage().equals("User not found"))
        .verify();

    verify(userService).getUserByEmail(email);
    verifyNoInteractions(passwordEncoder);
  }

  @Test
  void shouldFailWhenPasswordInvalid() {
    String email = "user@example.com";
    String rawPassword = "wrong";
    String hashedPassword = "hashed";

    var user = new User(email, hashedPassword);
    var authentication = new UsernamePasswordAuthenticationToken(email, rawPassword);

    when(userService.getUserByEmail(email)).thenReturn(Mono.just(user));
    when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

    customReactiveAuthenticationManager
        .authenticate(authentication)
        .as(StepVerifier::create)
        .expectErrorMatches(
            error ->
                error instanceof BadCredentialsException
                    && error.getMessage().equals("Invalid password"))
        .verify();

    verify(userService).getUserByEmail(email);
    verify(passwordEncoder).matches(rawPassword, hashedPassword);
  }
}
