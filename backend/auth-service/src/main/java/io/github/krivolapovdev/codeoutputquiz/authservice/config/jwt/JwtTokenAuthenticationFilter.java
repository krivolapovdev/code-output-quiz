package io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt;

import io.github.krivolapovdev.codeoutputquiz.authservice.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.authservice.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {
  private final JwtTokenProvider tokenProvider;

  @Override
  public @NonNull Mono<Void> filter(
      @NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    return Mono.defer(
        () -> {
          HttpCookie cookie = exchange.getRequest().getCookies().getFirst("accessToken");

          if (cookie == null) {
            return chain.filter(exchange);
          }

          String token = cookie.getValue();

          try {
            tokenProvider.validateTokenType(token, TokenType.ACCESS);
            Authentication authentication = tokenProvider.getAuthentication(token);
            return chain
                .filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
          } catch (JwtException e) {
            return chain.filter(exchange);
          }
        });
  }
}
