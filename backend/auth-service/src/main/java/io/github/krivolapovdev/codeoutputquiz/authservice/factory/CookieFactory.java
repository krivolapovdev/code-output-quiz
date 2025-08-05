package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieFactory {
  public @NonNull ResponseCookie create(
      @NonNull String cookieName, @NonNull String value, @NonNull String path, @NonNull Duration maxAge) {
    return ResponseCookie.from(cookieName, value)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path(path)
        .maxAge(maxAge)
        .build();
  }
}
