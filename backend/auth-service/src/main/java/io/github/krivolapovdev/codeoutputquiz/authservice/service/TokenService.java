package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.TokenPairFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
  private final JwtTokenProvider jwtTokenProvider;
  private final TokenPairFactory tokenPairFactory;
  private final AuthResponseFactory authResponseFactory;

  public @NonNull TokenPair generateTokens(@NonNull Authentication auth) {
    log.info("Generating tokens for user: {}", auth.getName());
    return tokenPairFactory.create(auth);
  }

  public Mono<ResponseEntity<AuthResponse>> refreshToken(@NonNull String oldTokenCookie) {
    log.info("Refreshing token: {}", oldTokenCookie);

    // noinspection ReactiveStreamsTooLongSameOperatorsChain
    return Mono.just(oldTokenCookie)
        .doOnNext(token -> jwtTokenProvider.validateTokenType(token, TokenType.REFRESH))
        .map(jwtTokenProvider::getAuthentication)
        .map(this::generateTokens)
        .map(
            tokenPair -> authResponseFactory.create(
                tokenPair.accessToken(), tokenPair.refreshToken(), HttpStatus.OK))
        .doOnError(error -> log.error("Failed to refresh token", error));
  }
}
