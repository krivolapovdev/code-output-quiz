package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseEntityFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
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

  private final JwtService jwtService;
  private final AuthResponseEntityFactory authResponseEntityFactory;

  public @NonNull TokenPair generateTokens(@NonNull Authentication auth) {
    log.info("Generating tokens for user: {}", auth.getName());
    String accessToken = jwtService.createAccessToken(auth);
    String refreshToken = jwtService.createRefreshToken(auth);
    return new TokenPair(accessToken, refreshToken);
  }

  public Mono<ResponseEntity<AuthResponse>> refreshToken(@NonNull String oldRefreshTokenCookie) {
    log.info("Refreshing token: {}", oldRefreshTokenCookie);

    // noinspection ReactiveStreamsTooLongSameOperatorsChain
    return Mono.just(oldRefreshTokenCookie)
        .doOnNext(token -> jwtService.validateToken(token, TokenType.REFRESH))
        .map(jwtService::parseAuthentication)
        .map(this::generateTokens)
        .map(tokenPair -> authResponseEntityFactory.create(tokenPair, HttpStatus.OK))
        .doOnError(error -> log.error("Failed to refresh token", error));
  }
}
