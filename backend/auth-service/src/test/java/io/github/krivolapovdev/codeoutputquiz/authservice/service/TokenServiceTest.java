package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseEntityFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
  @Mock private JwtService jwtService;
  @Mock private AuthResponseEntityFactory authResponseEntityFactory;
  @Mock private Authentication authentication;
  @InjectMocks private TokenService tokenService;

  @Test
  void shouldGenerateTokens() {
    String accessToken = "access-token";
    String refreshToken = "refresh-token";

    when(authentication.getName()).thenReturn("user@example.com");
    when(jwtService.createAccessToken(authentication)).thenReturn(accessToken);
    when(jwtService.createRefreshToken(authentication)).thenReturn(refreshToken);

    TokenPair tokenPair = tokenService.generateTokens(authentication);

    assertThat(tokenPair.accessToken()).isEqualTo(accessToken);
    assertThat(tokenPair.refreshToken()).isEqualTo(refreshToken);

    verify(jwtService).createAccessToken(authentication);
    verify(jwtService).createRefreshToken(authentication);
  }

  @Test
  void shouldRefreshToken() {
    String oldToken = "old-refresh-token";
    String newAccessToken = "new-access-token";
    String newRefreshToken = "new-refresh-token";
    ResponseEntity<AuthResponse> expectedResponse =
        ResponseEntity.status(HttpStatus.OK).body(new AuthResponse());

    doNothing().when(jwtService).validateToken(oldToken, TokenType.REFRESH);
    when(jwtService.parseAuthentication(oldToken)).thenReturn(authentication);
    when(authentication.getName()).thenReturn("user@example.com");
    when(jwtService.createAccessToken(authentication)).thenReturn(newAccessToken);
    when(jwtService.createRefreshToken(authentication)).thenReturn(newRefreshToken);
    when(authResponseEntityFactory.create(newAccessToken, newRefreshToken, HttpStatus.OK))
        .thenReturn(expectedResponse);

    tokenService
        .refreshToken(oldToken)
        .as(StepVerifier::create)
        .assertNext(response -> assertThat(response).isSameAs(expectedResponse))
        .verifyComplete();

    verify(jwtService).validateToken(oldToken, TokenType.REFRESH);
    verify(jwtService).parseAuthentication(oldToken);
    verify(jwtService).createAccessToken(authentication);
    verify(jwtService).createRefreshToken(authentication);
    verify(authResponseEntityFactory).create(newAccessToken, newRefreshToken, HttpStatus.OK);
  }
}
