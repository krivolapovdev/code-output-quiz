package io.github.nellshark.codeoutputquiz.authservice.config;

import io.github.nellshark.codeoutputquiz.authservice.config.jwt.JwtTokenAuthenticationFilter;
import io.github.nellshark.codeoutputquiz.authservice.config.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(
      ServerHttpSecurity http,
      JwtTokenProvider tokenProvider,
      ReactiveAuthenticationManager reactiveAuthenticationManager) {
    return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authenticationManager(reactiveAuthenticationManager)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Stateless
        .addFilterAt(
            new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults())
        .build();
  }
}
