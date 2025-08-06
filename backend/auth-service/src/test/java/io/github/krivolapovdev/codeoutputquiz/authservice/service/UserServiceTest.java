package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.UserFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.notifier.UserRegistrationNotifier;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private UserFactory userFactory;
  @Mock private UserRegistrationNotifier userRegistrationNotifier;
  @InjectMocks private UserService userService;

  @Test
  void shouldSaveUserSuccessfully() {
    AuthRequest request = new AuthRequest("test@example.com", "password123");
    User user = new User(request.email(), "encodedPassword");

    when(userFactory.create(request)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(Mono.just(user));
    when(userRegistrationNotifier.notify(user.getEmail())).thenReturn(Mono.empty());

    userService
        .saveUser(request)
        .as(StepVerifier::create)
        .assertNext(saved -> assertThat(saved.getEmail()).isEqualTo(request.email()))
        .verifyComplete();

    verify(userFactory).create(request);
    verify(userRepository).save(user);
    verify(userRegistrationNotifier).notify(user.getEmail());
    verifyNoMoreInteractions(userFactory, userRepository, userRegistrationNotifier);
  }

  @Test
  void shouldFailToSaveWhenEmailAlreadyExists() {
    AuthRequest request = new AuthRequest("taken@example.com", "password123");
    User user = new User(request.email(), "hashed");

    when(userFactory.create(request)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(Mono.error(new DuplicateKeyException("duplicate")));

    userService
        .saveUser(request)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            error -> {
              assertThat(error).isInstanceOf(EmailAlreadyTakenException.class);
              assertThat(error.getMessage()).isEqualTo("Email already exists");
            })
        .verify();

    verify(userFactory).create(request);
    verify(userRepository).save(user);
    verifyNoMoreInteractions(userFactory, userRepository);
    verifyNoInteractions(userRegistrationNotifier);
  }

  @Test
  void shouldFindUserByEmail() {
    String email = "found@example.com";
    User user = new User(email, "hashed");

    when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));

    userService
        .getUserByEmail(email)
        .as(StepVerifier::create)
        .assertNext(found -> assertThat(found.getEmail()).isEqualTo(email))
        .verifyComplete();

    verify(userRepository).findByEmail(email);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldFailWhenUserNotFound() {
    String email = "missing@example.com";

    when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

    userService
        .getUserByEmail(email)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            error -> {
              assertThat(error).isInstanceOf(UsernameNotFoundException.class);
              assertThat(error.getMessage()).isEqualTo("User not found: " + email);
            })
        .verify();

    verify(userRepository).findByEmail(email);
    verifyNoMoreInteractions(userRepository);
  }
}
