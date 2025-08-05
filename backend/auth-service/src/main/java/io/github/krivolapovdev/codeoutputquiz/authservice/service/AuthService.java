package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.CookieFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.UserFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.mapper.AuthRequestMapper;
import io.github.krivolapovdev.codeoutputquiz.authservice.notifier.UserRegistrationNotifier;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final UserFactory userFactory;
  private final CookieFactory cookieFactory;
  private final TokenService tokenService;
  private final ReactiveAuthenticationManager reactiveAuthenticationManager;
  private final AuthResponseFactory authResponseFactory;
  private final AuthRequestMapper authRequestMapper;
  private final UserRegistrationNotifier userRegistrationNotifier;

  public Mono<ResponseEntity<AuthResponse>> register(@NonNull AuthRequest request) {
    log.info("Attempting to register user: {}", request.email());
    return Mono.fromCallable(() -> userFactory.create(request))
        .flatMap(userRepository::save)
        .doOnSuccess(user -> log.info("User registered successfully: {}", user.getEmail()))
        .map(ignored -> authRequestMapper.toAuthentication(request))
        .flatMap(reactiveAuthenticationManager::authenticate)
        .map(tokenService::generateTokens)
        .map(
            tokenPair ->
                authResponseFactory.create(
                    tokenPair.accessToken(), tokenPair.refreshToken(), HttpStatus.CREATED))
        .flatMap(response -> userRegistrationNotifier.notify(request.email()).thenReturn(response))
        .onErrorMap(
            DuplicateKeyException.class,
            e -> new EmailAlreadyTakenException("Email already exists"));
  }

  public Mono<ResponseEntity<AuthResponse>> login(@NonNull AuthRequest request) {
    log.info("Logging in user: {}", request.email());

    var token = authRequestMapper.toAuthentication(request);
    return reactiveAuthenticationManager
        .authenticate(token)
        .map(tokenService::generateTokens)
        .map(
            tokenPair ->
                authResponseFactory.create(
                    tokenPair.accessToken(), tokenPair.refreshToken(), HttpStatus.OK))
        .doOnError(error -> log.error("Login failed for user: {}", request.email(), error));
  }

  public Mono<ResponseEntity<Void>> logout() {
    log.info("Logging out user");

    var accessCookie = cookieFactory.create(CookieNames.ACCESS_TOKEN, "", "/", Duration.ZERO);
    var refreshCookie =
        cookieFactory.create(CookieNames.REFRESH_TOKEN, "", "/api/v1/auth/refresh", Duration.ZERO);

    return Mono.just(
        ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .build());
  }
}
