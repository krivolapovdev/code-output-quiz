package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.config.converter.AnswerChoiceDataReadConverter;
import io.r2dbc.spi.ConnectionFactory;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;

@TestConfiguration
public class TestDatabaseConfig {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(
      ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
    var dialect = DialectResolver.getDialect(connectionFactory);
    var converters = List.of(new AnswerChoiceDataReadConverter(objectMapper));
    return R2dbcCustomConversions.of(dialect, converters);
  }
}
