package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import io.github.krivolapovdev.codeoutputquiz.authservice.service.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthResponseEntityFactory {
  private final CookieService cookieService;

  public @NonNull ResponseEntity<AuthResponse> create(
      @NonNull TokenPair tokenPair, @NonNull HttpStatus status) {

    ResponseCookie accessCookie = cookieService.createAccessTokenCookie(tokenPair.accessToken());
    ResponseCookie refreshCookie = cookieService.createRefreshTokenCookie(tokenPair.refreshToken());

    return ResponseEntity.status(status)
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
        .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        .body(new AuthResponse());
  }
}
