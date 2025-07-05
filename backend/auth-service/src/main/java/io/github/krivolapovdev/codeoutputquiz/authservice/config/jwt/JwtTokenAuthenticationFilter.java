package io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt;

import io.github.krivolapovdev.codeoutputquiz.authservice.security.jwt.JwtTokenProvider;
import io.github.krivolapovdev.codeoutputquiz.authservice.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
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
    return JwtUtils.extractAccessToken(
            exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .map(tokenProvider::getAuthentication)
        .map(
            authentication ->
                chain
                    .filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)))
        .orElseGet(() -> chain.filter(exchange));
  }
}
