package io.github.krivolapovdev.codeoutputquiz.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class FlywayDevMigrationConfig {
  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return flyway -> {
      try {
        flyway.migrate();
      } catch (FlywayException ex) {
        log.warn("Flyway migration failed. Cleaning and retrying migration.", ex);
        flyway.clean();
        flyway.migrate();
      }
    };
  }
}
