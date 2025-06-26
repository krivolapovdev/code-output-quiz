package io.github.nellshark.codeoutputquiz.authservice.service;

import io.github.nellshark.codeoutputquiz.authservice.entity.User;
import io.github.nellshark.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.nellshark.codeoutputquiz.authservice.repository.UserRepository;
import io.github.nellshark.codeoutputquiz.authservice.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Mono<User> register(RegistrationRequest registrationRequest) {
    log.info("Registering user {}:", registrationRequest.email());

    User user =
        new User(
            registrationRequest.email(), passwordEncoder.encode(registrationRequest.password()));

    return userRepository
        .findByEmail(registrationRequest.email())
        .flatMap(u -> Mono.error(new EmailAlreadyTakenException("Email already exists")))
        .switchIfEmpty(Mono.defer(() -> userRepository.save(user)))
        .cast(User.class);
  }
}
