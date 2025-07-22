package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.event.EmailNotificationEvent;
import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.producer.EmailNotificationProducer;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.security.auth.CustomReactiveAuthenticationManager;
import io.github.krivolapovdev.codeoutputquiz.authservice.security.jwt.JwtTokenProvider;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomReactiveAuthenticationManager customReactiveAuthenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailNotificationProducer emailNotificationProducer;

  public Mono<ResponseEntity<AuthResponse>> register(AuthRequest request) {
    log.info("Attempting to register user: {}", request.email());

    return createAndSaveUser(request)
        .doOnSuccess(user -> log.info("User registered successfully: {}", user.getEmail()))
        .flatMap(user -> sendWelcomeEmail(user).thenReturn(user))
        .flatMap(
            user ->
                authenticateAndBuildResponse(
                    user.getEmail(), request.password(), HttpStatus.CREATED))
        .onErrorMap(
            DuplicateKeyException.class,
            e -> new EmailAlreadyTakenException("Email already exists"));
  }

  public Mono<ResponseEntity<AuthResponse>> login(AuthRequest request) {
    log.info("Logging in user: {}", request.email());
    return authenticateAndBuildResponse(request.email(), request.password(), HttpStatus.OK);
  }

  public Mono<ResponseEntity<AuthResponse>> refreshToken(String oldTokenCookie) {
    log.info("Refreshing token: {}", oldTokenCookie);
    return Mono.just(oldTokenCookie)
        .doOnNext(jwtTokenProvider::validateRefreshToken)
        .map(jwtTokenProvider::getAuthentication)
        .map(
            authentication -> {
              AuthDetails authDetails = (AuthDetails) authentication.getDetails();
              String accessToken =
                  jwtTokenProvider.createAccessToken(authentication, authDetails.userId());
              String refreshToken =
                  jwtTokenProvider.createRefreshToken(authentication, authDetails.userId());
              return buildAuthResponseEntity(accessToken, refreshToken, HttpStatus.OK);
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
    return customReactiveAuthenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
        .map(
            auth -> {
              AuthDetails authDetails = (AuthDetails) auth.getDetails();
              UUID userId = authDetails.userId();
              String accessToken = jwtTokenProvider.createAccessToken(auth, userId);
              String refreshToken = jwtTokenProvider.createRefreshToken(auth, userId);
              return buildAuthResponseEntity(accessToken, refreshToken, status);
            });
  }

  private ResponseEntity<AuthResponse> buildAuthResponseEntity(
      String accessToken, String refreshToken, HttpStatus status) {

    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/api/v1/auth/refresh")
            .maxAge(Duration.ofDays(7))
            .sameSite("Strict")
            .build();

    return ResponseEntity.status(status)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new AuthResponse(accessToken));
  }

  private Mono<Void> sendWelcomeEmail(User user) {
    var event =
        new EmailNotificationEvent(
            user.getEmail(),
            "Welcome to CodeOutputQuiz!",
            """
        Thanks for registering, %s!

        You can now enjoy solving programming output questions 🚀

        Want to contribute or give feedback?
        Visit our GitHub: https://github.com/krivolapovdev/code-output-quiz
        """
                .formatted(user.getEmail()));

    return emailNotificationProducer
        .sendEmailNotificationEvent(event)
        .doOnSuccess(unused -> log.info("Welcome email sent to {}", user.getEmail()))
        .doOnError(error -> log.error("Failed to send welcome email to {}", user.getEmail(), error))
        .then()
        .onErrorResume(e -> Mono.empty());
  }
}
