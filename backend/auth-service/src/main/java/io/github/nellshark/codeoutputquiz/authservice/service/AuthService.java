package io.github.nellshark.codeoutputquiz.authservice.service;

import io.github.nellshark.codeoutputquiz.authservice.config.jwt.JwtTokenProvider;
import io.github.nellshark.codeoutputquiz.authservice.entity.User;
import io.github.nellshark.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.nellshark.codeoutputquiz.authservice.repository.UserRepository;
import io.github.nellshark.codeoutputquiz.authservice.request.AuthRequest;
import io.github.nellshark.codeoutputquiz.authservice.request.RegistrationRequest;
import io.github.nellshark.codeoutputquiz.authservice.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final JwtTokenProvider tokenProvider;
  private final ReactiveAuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Mono<User> register(RegistrationRequest registrationRequest) {
    log.info("Registering user: {}", registrationRequest.email());

    User user =
        new User(
            registrationRequest.email(), passwordEncoder.encode(registrationRequest.password()));

    return userRepository
        .findByEmail(registrationRequest.email())
        .flatMap(u -> Mono.error(new EmailAlreadyTakenException("Email already exists")))
        .switchIfEmpty(Mono.defer(() -> userRepository.save(user)))
        .cast(User.class);
  }

  public Mono<ResponseEntity<AuthResponse>> login(Mono<AuthRequest> authRequest) {
    return authRequest
        .doOnNext(req -> log.info("Logging in user: {}", req.email()))
        .flatMap(
            login ->
                authenticationManager
                    .authenticate(
                        new UsernamePasswordAuthenticationToken(login.email(), login.password()))
                    .map(tokenProvider::createToken))
        .map(
            jwt -> {
              HttpHeaders httpHeaders = new HttpHeaders();
              httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
              AuthResponse authResponse = new AuthResponse(jwt);
              return new ResponseEntity<>(authResponse, httpHeaders, HttpStatus.OK);
            });
  }
}
