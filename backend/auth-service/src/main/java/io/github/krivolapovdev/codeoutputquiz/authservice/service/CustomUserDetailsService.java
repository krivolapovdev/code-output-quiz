package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {
  private final UserRepository userRepository;

  @Override
  public Mono<UserDetails> findByUsername(String email) {
    return userRepository
        .findByEmail(email.toLowerCase())
        .map(
            user ->
                User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles("USER")
                    .build());
  }
}
