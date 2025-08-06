package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseEntityFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthDetails;
import java.util.UUID;
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
  @Mock private AuthDetails authDetails;
  @InjectMocks private TokenService tokenService;

  @Test
  void shouldGenerateTokens() {
    UUID userId = UUID.randomUUID();
    String accessToken = "access-token";
    String refreshToken = "refresh-token";

    when(authentication.getDetails()).thenReturn(authDetails);
    when(authentication.getName()).thenReturn("user@example.com");
    when(authDetails.userId()).thenReturn(userId);
    when(jwtService.createAccessToken(authentication, userId)).thenReturn(accessToken);
    when(jwtService.createRefreshToken(authentication, userId)).thenReturn(refreshToken);

    TokenPair tokenPair = tokenService.generateTokens(authentication);

    assertThat(tokenPair.accessToken()).isEqualTo(accessToken);
    assertThat(tokenPair.refreshToken()).isEqualTo(refreshToken);

    verify(authentication).getDetails();
    verify(authentication).getName();
    verify(authDetails, times(2)).userId();
    verify(jwtService).createAccessToken(authentication, userId);
    verify(jwtService).createRefreshToken(authentication, userId);
  }

  @Test
  void shouldRefreshToken() {
    String oldToken = "old-refresh-token";
    String newAccessToken = "new-access-token";
    String newRefreshToken = "new-refresh-token";
    UUID userId = UUID.randomUUID();
    ResponseEntity<AuthResponse> expectedResponse =
        ResponseEntity.status(HttpStatus.OK).body(new AuthResponse());

    doNothing().when(jwtService).validateToken(oldToken, TokenType.REFRESH);
    when(jwtService.parseAuthentication(oldToken)).thenReturn(authentication);
    when(authentication.getDetails()).thenReturn(authDetails);
    when(authentication.getName()).thenReturn("user@example.com");
    when(authDetails.userId()).thenReturn(userId);
    when(jwtService.createAccessToken(authentication, userId)).thenReturn(newAccessToken);
    when(jwtService.createRefreshToken(authentication, userId)).thenReturn(newRefreshToken);
    when(authResponseEntityFactory.create(newAccessToken, newRefreshToken, HttpStatus.OK))
        .thenReturn(expectedResponse);

    tokenService
        .refreshToken(oldToken)
        .as(StepVerifier::create)
        .assertNext(response -> assertThat(response).isSameAs(expectedResponse))
        .verifyComplete();

    verify(jwtService).validateToken(oldToken, TokenType.REFRESH);
    verify(jwtService).parseAuthentication(oldToken);
    verify(authentication).getDetails();
    verify(authDetails, times(2)).userId();
    verify(jwtService).createAccessToken(authentication, userId);
    verify(jwtService).createRefreshToken(authentication, userId);
    verify(authResponseEntityFactory).create(newAccessToken, newRefreshToken, HttpStatus.OK);
  }
}
