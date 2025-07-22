package io.github.krivolapovdev.codeoutputquiz.quizservice.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class FlywayDevMigrationConfig {
  @Bean
  public FlywayMigrationStrategy flywayCleanMigrationStrategy() {
    return flyway -> {
      flyway.clean();
      flyway.migrate();
    };
  }
}
