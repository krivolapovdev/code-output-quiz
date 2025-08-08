package io.github.krivolapovdev.codeoutputquiz.quizservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class FlywayDevMigrationConfig {
  @Bean
  public FlywayMigrationStrategy conditionalCleanOnFailureStrategy() {
    return flyway -> {
      try {
        flyway.migrate();
      } catch (Exception ex) {
        log.warn("Flyway migration failed. Cleaning and retrying migration.", ex);
        flyway.clean();
        flyway.migrate();
      }
    };
  }
}
