package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthResponseFactory {
  private final CookieFactory cookieFactory;

  public @NonNull ResponseEntity<AuthResponse> create(
      @NonNull String accessToken, @NonNull String refreshToken, @NonNull HttpStatus status) {
    ResponseCookie accessCookie =
        cookieFactory.create(CookieNames.ACCESS_TOKEN, accessToken, "/", Duration.ofMinutes(15));

    ResponseCookie refreshCookie =
        cookieFactory.create(
            CookieNames.REFRESH_TOKEN, refreshToken, "/api/v1/auth/refresh", Duration.ofDays(7));

    return ResponseEntity.status(status)
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
        .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        .body(new AuthResponse());
  }
}
