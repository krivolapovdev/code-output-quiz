package io.github.krivolapovdev.codeoutputquiz.authservice.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@TestConfiguration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class TestSecurityConfig {
  @Bean
  public SecurityWebFilterChain testSecurityWebFilterChain(ServerHttpSecurity http) {
    return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
        .build();
  }
}
