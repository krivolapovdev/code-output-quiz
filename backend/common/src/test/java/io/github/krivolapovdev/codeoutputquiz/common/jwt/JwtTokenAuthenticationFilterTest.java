package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import io.github.krivolapovdev.codeoutputquiz.common.cookie.CookieNames;
import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.jsonwebtoken.JwtException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@ExtendWith(MockitoExtension.class)
class JwtTokenAuthenticationFilterTest {
  @Mock private JwtTokenProvider tokenProvider;
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    RouterFunction<ServerResponse> router =
        RouterFunctions.route(
            GET("/me"),
            req ->
                ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::getName)
                    .defaultIfEmpty("anonymous")
                    .flatMap(name -> ServerResponse.ok().bodyValue(name)));

    JwtTokenAuthenticationFilter filter = new JwtTokenAuthenticationFilter(tokenProvider);

    this.webTestClient =
        WebTestClient.bindToRouterFunction(router).webFilter(filter).configureClient().build();
  }

  @Test
  void shouldPassThroughWithoutCookieAndNotAuthenticate() {
    webTestClient
        .get()
        .uri("/me")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("anonymous");

    verifyNoInteractions(tokenProvider);
  }

  @Test
  void shouldIgnoreInvalidTokenAndNotSetAuthentication() {
    String bad = "bad.jwt";
    doThrow(new JwtException("invalid"))
        .when(tokenProvider)
        .validateTokenType(bad, TokenType.ACCESS);

    webTestClient
        .get()
        .uri("/me")
        .cookie(CookieNames.ACCESS_TOKEN, bad)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("anonymous");

    verify(tokenProvider).validateTokenType(bad, TokenType.ACCESS);
    verify(tokenProvider, never()).getAuthentication(anyString());
  }

  @Test
  void shouldSetAuthenticationFromValidToken() {
    String good = "good.jwt";
    Authentication auth =
        new UsernamePasswordAuthenticationToken("user@example.com", null, Collections.emptyList());

    doNothing().when(tokenProvider).validateTokenType(good, TokenType.ACCESS);
    when(tokenProvider.getAuthentication(good)).thenReturn(auth);

    webTestClient
        .get()
        .uri("/me")
        .cookie(CookieNames.ACCESS_TOKEN, good)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("user@example.com");

    verify(tokenProvider).validateTokenType(good, TokenType.ACCESS);
    verify(tokenProvider).getAuthentication(good);
  }
}
