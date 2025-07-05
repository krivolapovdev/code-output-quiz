package io.github.krivolapovdev.codeoutputquiz.userservice.config.jwt;

import io.github.krivolapovdev.codeoutputquiz.userservice.security.jwt.JwtTokenProvider;
import io.github.krivolapovdev.codeoutputquiz.userservice.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
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
    return JwtUtils.extractAccessToken(
            exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .map(
            token -> {
              try {
                var auth = tokenProvider.getAuthentication(token);
                return chain
                    .filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
              } catch (JwtException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT", ex);
              }
            })
        .orElseGet(() -> chain.filter(exchange));
  }
}
