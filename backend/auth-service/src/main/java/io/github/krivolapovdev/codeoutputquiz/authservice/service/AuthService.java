package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.JwtTokenProvider;
import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.util.JwtUtils;
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
  private final JwtTokenProvider jwtTokenProvider;
  private final ReactiveAuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Mono<ResponseEntity<AuthResponse>> register(AuthRequest request) {
    log.info("Registering user: {}", request.email());

    return userRepository
        .findByEmail(request.email())
        .flatMap(u -> Mono.error(new EmailAlreadyTakenException("Email already exists")))
        .switchIfEmpty(Mono.defer(() -> createAndSaveUser(request)))
        .flatMap(
            savedUser ->
                authenticateAndBuildResponse(
                    request.email(), request.password(), HttpStatus.CREATED));
  }

  public Mono<ResponseEntity<AuthResponse>> login(AuthRequest request) {
    log.info("Logging in user: {}", request.email());
    return authenticateAndBuildResponse(request.email(), request.password(), HttpStatus.OK);
  }

  public Mono<ResponseEntity<AuthResponse>> refreshToken(String oldTokenHeader) {
    log.info("Refreshing token: {}", oldTokenHeader);
    return Mono.justOrEmpty(JwtUtils.extractToken(oldTokenHeader))
        .doOnNext(jwtTokenProvider::validateRefreshToken)
        .map(jwtTokenProvider::getAuthentication)
        .map(
            authentication -> {
              String accessToken = jwtTokenProvider.createAccessToken(authentication);
              String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
              return buildAuthResponse(accessToken, refreshToken, HttpStatus.OK);
            });
  }

  private Mono<User> createAndSaveUser(AuthRequest request) {
    log.info("Creating new user with email: {}", request.email());
    User newUser = new User(request.email(), passwordEncoder.encode(request.password()));
    return userRepository.save(newUser);
  }

  private Mono<ResponseEntity<AuthResponse>> authenticateAndBuildResponse(
      String email, String password, HttpStatus status) {
    log.info("Authenticating user with email: {}", email);
    return authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
        .map(
            authentication -> {
              String accessToken = jwtTokenProvider.createAccessToken(authentication);
              String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
              return buildAuthResponse(accessToken, refreshToken, status);
            });
  }

  private ResponseEntity<AuthResponse> buildAuthResponse(
      String accessToken, String refreshToken, HttpStatus status) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
    return new ResponseEntity<>(authResponse, headers, status);
  }
}
