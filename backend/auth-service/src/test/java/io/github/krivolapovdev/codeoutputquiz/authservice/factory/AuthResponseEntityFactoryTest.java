package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.CookieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthResponseEntityFactoryTest {
  @Mock private CookieService cookieService;
  @InjectMocks AuthResponseEntityFactory authResponseEntityFactory;

  @Test
  void shouldCreateResponseEntityWithCookies() {
    String accessToken = "access-token";
    String refreshToken = "refresh-token";
    TokenPair tokenPair = new TokenPair(accessToken, refreshToken);
    HttpStatus status = HttpStatus.CREATED;

    ResponseCookie accessCookie = ResponseCookie.from("AccessToken", accessToken).build();
    ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", refreshToken).build();

    when(cookieService.createAccessTokenCookie(accessToken)).thenReturn(accessCookie);
    when(cookieService.createRefreshTokenCookie(refreshToken)).thenReturn(refreshCookie);

    ResponseEntity<AuthResponse> response = authResponseEntityFactory.create(tokenPair, status);

    assertThat(response.getStatusCode()).isEqualTo(status);
    assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE))
        .containsExactlyInAnyOrder(accessCookie.toString(), refreshCookie.toString());
    assertThat(response.getBody()).isNotNull().isInstanceOf(AuthResponse.class);

    verify(cookieService).createAccessTokenCookie(accessToken);
    verify(cookieService).createRefreshTokenCookie(refreshToken);
  }
}
