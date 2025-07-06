package io.github.krivolapovdev.codeoutputquiz.authservice.config;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.JwtTokenAuthenticationFilter;
import io.github.krivolapovdev.codeoutputquiz.authservice.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(
      ServerHttpSecurity http,
      JwtTokenProvider jwtTokenProvider,
      ReactiveAuthenticationManager reactiveAuthenticationManager) {
    return http.csrf(CsrfSpec::disable)
        .authenticationManager(reactiveAuthenticationManager)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Stateless
        .addFilterAt(
            new JwtTokenAuthenticationFilter(jwtTokenProvider),
            SecurityWebFiltersOrder.AUTHENTICATION)
        .httpBasic(HttpBasicSpec::disable)
        .formLogin(FormLoginSpec::disable)
        .build();
  }
}
