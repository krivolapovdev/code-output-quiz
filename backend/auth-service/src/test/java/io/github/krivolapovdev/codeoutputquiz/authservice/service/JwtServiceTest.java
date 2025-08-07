package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
  @Mock private JwtTokenProvider jwtTokenProvider;
  @Mock private Authentication authentication;
  @InjectMocks private JwtService jwtService;

  @Test
  void shouldValidateToken() {
    String token = "some-token";
    TokenType type = TokenType.ACCESS;

    jwtService.validateToken(token, type);

    verify(jwtTokenProvider).validateTokenType(token, type);
  }

  @Test
  void shouldParseAuthentication() {
    String token = "some-token";
    when(jwtTokenProvider.getAuthentication(token)).thenReturn(authentication);

    Authentication result = jwtService.parseAuthentication(token);

    assertThat(result).isSameAs(authentication);
    verify(jwtTokenProvider).getAuthentication(token);
  }

  @Test
  void shouldCreateAccessToken() {
    String expectedToken = "access-token";

    when(jwtTokenProvider.createToken(TokenType.ACCESS, authentication)).thenReturn(expectedToken);

    String result = jwtService.createAccessToken(authentication);

    assertThat(result).isEqualTo(expectedToken);
    verify(jwtTokenProvider).createToken(TokenType.ACCESS, authentication);
  }

  @Test
  void shouldCreateRefreshToken() {
    String expectedToken = "refresh-token";

    when(jwtTokenProvider.createToken(TokenType.REFRESH, authentication)).thenReturn(expectedToken);

    String result = jwtService.createRefreshToken(authentication);

    assertThat(result).isEqualTo(expectedToken);
    verify(jwtTokenProvider).createToken(TokenType.REFRESH, authentication);
  }
}
