package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.factory.CookieFactory;
import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {
  @Mock private CookieFactory cookieFactory;
  @InjectMocks private CookieService cookieService;

  @Test
  void shouldCreateAccessTokenCookie() {
    String token = "access-token";
    ResponseCookie expectedCookie = ResponseCookie.from(CookieNames.ACCESS_TOKEN, token).build();

    when(cookieFactory.create(CookieNames.ACCESS_TOKEN, token, "/", Duration.ofMinutes(15)))
        .thenReturn(expectedCookie);

    ResponseCookie actualCookie = cookieService.createAccessTokenCookie(token);

    assertThat(actualCookie).isSameAs(expectedCookie);

    verify(cookieFactory).create(CookieNames.ACCESS_TOKEN, token, "/", Duration.ofMinutes(15));
  }

  @Test
  void shouldCreateRefreshTokenCookie() {
    String token = "refresh-token";
    ResponseCookie expectedCookie = ResponseCookie.from(CookieNames.REFRESH_TOKEN, token).build();

    when(cookieFactory.create(
            CookieNames.REFRESH_TOKEN, token, "/api/v1/auth/refresh", Duration.ofDays(7)))
        .thenReturn(expectedCookie);

    ResponseCookie actualCookie = cookieService.createRefreshTokenCookie(token);

    assertThat(actualCookie).isSameAs(expectedCookie);

    verify(cookieFactory)
        .create(CookieNames.REFRESH_TOKEN, token, "/api/v1/auth/refresh", Duration.ofDays(7));
  }

  @Test
  void shouldClearAccessTokenCookie() {
    ResponseCookie expectedCookie =
        ResponseCookie.from(CookieNames.ACCESS_TOKEN, "").maxAge(Duration.ZERO).build();

    when(cookieFactory.create(CookieNames.ACCESS_TOKEN, "", "/", Duration.ZERO))
        .thenReturn(expectedCookie);

    ResponseCookie actualCookie = cookieService.clearAccessTokenCookie();

    assertThat(actualCookie).isSameAs(expectedCookie);

    verify(cookieFactory).create(CookieNames.ACCESS_TOKEN, "", "/", Duration.ZERO);
  }

  @Test
  void shouldClearRefreshTokenCookie() {
    ResponseCookie expectedCookie =
        ResponseCookie.from(CookieNames.REFRESH_TOKEN, "").maxAge(Duration.ZERO).build();

    when(cookieFactory.create(CookieNames.REFRESH_TOKEN, "", "/api/v1/auth/refresh", Duration.ZERO))
        .thenReturn(expectedCookie);

    ResponseCookie actualCookie = cookieService.clearRefreshTokenCookie();

    assertThat(actualCookie).isSameAs(expectedCookie);

    verify(cookieFactory)
        .create(CookieNames.REFRESH_TOKEN, "", "/api/v1/auth/refresh", Duration.ZERO);
  }
}
