package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.security.auth.CustomReactiveAuthenticationManager;
import io.github.krivolapovdev.codeoutputquiz.authservice.security.jwt.JwtTokenProvider;
import io.github.krivolapovdev.codeoutputquiz.authservice.util.JwtUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

  public Mono<ResponseEntity<AuthResponse>> register(AuthRequest request) {
    log.info("Registering user: {}", request.email());

    return createAndSaveUser(request)
        .flatMap(
            savedUser ->
                authenticateAndBuildResponse(
                    request.email(), request.password(), HttpStatus.CREATED))
        .onErrorMap(
            DuplicateKeyException.class,
            e -> new EmailAlreadyTakenException("Email already exists"));
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
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
    return new ResponseEntity<>(authResponse, headers, status);
  }
}
