package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseEntityFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.mapper.AuthRequestMapper;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
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

  private final CookieService cookieService;
  private final UserService userService;
  private final TokenService tokenService;
  private final ReactiveAuthenticationManager reactiveAuthenticationManager;
  private final AuthResponseEntityFactory authResponseFactory;
  private final AuthRequestMapper authRequestMapper;

  public Mono<ResponseEntity<AuthResponse>> register(@NonNull AuthRequest request) {
    log.info("Attempting to register user: {}", request.email());
    return userService
        .saveUser(request)
        .map(ignored -> authRequestMapper.toAuthentication(request))
        .flatMap(reactiveAuthenticationManager::authenticate)
        .map(tokenService::generateTokens)
        .map(tokenPair -> authResponseFactory.create(tokenPair, HttpStatus.CREATED))
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
        .map(tokenPair -> authResponseFactory.create(tokenPair, HttpStatus.OK))
        .doOnError(error -> log.error("Login failed for user: {}", request.email(), error));
  }

  public Mono<ResponseEntity<Void>> logout() {
    log.info("Logging out user");

    var accessCookie = cookieService.clearAccessTokenCookie();
    var refreshCookie = cookieService.clearRefreshTokenCookie();

    return Mono.just(
        ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .build());
  }
}
