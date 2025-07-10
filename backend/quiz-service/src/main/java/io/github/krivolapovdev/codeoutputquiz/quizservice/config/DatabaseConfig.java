package io.github.krivolapovdev.codeoutputquiz.quizservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.config.converter.AnswerChoiceDataReadConverter;
import io.r2dbc.spi.ConnectionFactory;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;

@Configuration
public class DatabaseConfig {
  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(
      ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
    var dialect = DialectResolver.getDialect(connectionFactory);
    var converters = List.of(new AnswerChoiceDataReadConverter(objectMapper));
    return R2dbcCustomConversions.of(dialect, converters);
  }
}
