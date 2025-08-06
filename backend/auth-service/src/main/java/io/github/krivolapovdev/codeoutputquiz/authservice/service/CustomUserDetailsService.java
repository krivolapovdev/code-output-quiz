package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
class CustomUserDetailsService implements ReactiveUserDetailsService {
  private final UserService userService;

  @Override
  public Mono<UserDetails> findByUsername(@NonNull String email) {
    return userService
        .getUserByEmail(email.toLowerCase())
        .map(
            user ->
                User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build());
  }
}
