package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
  private final JwtTokenProvider jwtTokenProvider;

  public void validateToken(@NonNull String token, @NonNull TokenType type) {
    jwtTokenProvider.validateTokenType(token, type);
  }

  public @NonNull Authentication parseAuthentication(@NonNull String token) {
    return jwtTokenProvider.getAuthentication(token);
  }

  public @NonNull String createAccessToken(@NonNull Authentication auth) {
    return jwtTokenProvider.createToken(TokenType.ACCESS, auth);
  }

  public @NonNull String createRefreshToken(@NonNull Authentication auth) {
    return jwtTokenProvider.createToken(TokenType.REFRESH, auth);
  }
}
