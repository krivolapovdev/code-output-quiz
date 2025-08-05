package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenPairFactory {
  private final JwtTokenProvider jwtTokenProvider;

  public @NonNull TokenPair create(@NonNull Authentication auth) {
    AuthDetails details = (AuthDetails) auth.getDetails();
    String accessToken = jwtTokenProvider.createToken(TokenType.ACCESS, auth, details.userId());
    String refreshToken = jwtTokenProvider.createToken(TokenType.REFRESH, auth, details.userId());
    return new TokenPair(accessToken, refreshToken);
  }
}
