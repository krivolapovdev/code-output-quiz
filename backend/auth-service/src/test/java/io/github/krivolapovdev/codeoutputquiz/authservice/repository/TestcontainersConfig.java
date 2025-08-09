package io.github.krivolapovdev.codeoutputquiz.authservice.repository;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {
  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgres() {
    return new PostgreSQLContainer<>("postgres:17.5-alpine3.22");
  }
}
