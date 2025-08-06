package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import io.github.krivolapovdev.codeoutputquiz.authservice.factory.CookieFactory;
import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CookieService {
  private static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(15);
  private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);

  private final CookieFactory cookieFactory;

  public @NonNull ResponseCookie createAccessTokenCookie(@NonNull String token) {
    return cookieFactory.create(
        CookieNames.ACCESS_TOKEN, token, "/", ACCESS_TOKEN_DURATION);
  }

  public @NonNull ResponseCookie createRefreshTokenCookie(@NonNull String token) {
    return cookieFactory.create(
        CookieNames.REFRESH_TOKEN, token, "/api/v1/auth/refresh", REFRESH_TOKEN_DURATION);
  }

  public ResponseCookie clearAccessTokenCookie() {
    return cookieFactory.create(CookieNames.ACCESS_TOKEN, "", "/", Duration.ZERO);
  }

  public ResponseCookie clearRefreshTokenCookie() {
    return cookieFactory.create(CookieNames.REFRESH_TOKEN, "", "/api/v1/auth/refresh", Duration.ZERO);
  }
}
