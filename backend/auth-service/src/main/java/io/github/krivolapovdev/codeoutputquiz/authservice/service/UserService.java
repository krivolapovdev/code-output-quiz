package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.UserFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.notifier.UserRegistrationNotifier;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserFactory userFactory;
  private final UserRegistrationNotifier userRegistrationNotifier;

  public Mono<User> saveUser(@NonNull AuthRequest request) {
    log.info("Creating user from request: {}", request.email());

    return Mono.fromCallable(() -> userFactory.create(request))
        .flatMap(userRepository::save)
        .doOnSuccess(user -> log.info("User successfully created: {}", user.getEmail()))
        .flatMap(user -> userRegistrationNotifier.notify(user.getEmail()).thenReturn(user))
        .onErrorMap(
            DuplicateKeyException.class,
            ex -> new EmailAlreadyTakenException("Email already exists"));
  }

  public Mono<User> getUserByEmail(@NonNull String email) {
    log.info("Finding user by email: {}", email);
    return userRepository
        .findByEmail(email)
        .doOnNext(user -> log.info("Found user: {}", user.getEmail()))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + email)));
  }
}
