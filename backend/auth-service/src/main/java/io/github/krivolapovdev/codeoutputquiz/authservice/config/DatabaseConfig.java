package io.github.krivolapovdev.codeoutputquiz.authservice.config;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.converter.LocalDateTimeToOffsetDateTimeConverter;
import io.github.krivolapovdev.codeoutputquiz.authservice.config.converter.OffsetDateTimeToLocalDateTimeConverter;
import io.r2dbc.spi.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;

@Configuration
@EnableR2dbcAuditing
class DatabaseConfig {

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
    R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);

    List<Object> storeConverters = new ArrayList<>(dialect.getConverters());
    storeConverters.addAll(R2dbcCustomConversions.STORE_CONVERTERS);

    CustomConversions.StoreConversions storeConversions =
        CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder(), storeConverters);

    List<Converter<?, ?>> customConverters =
        List.of(
            new OffsetDateTimeToLocalDateTimeConverter(),
            new LocalDateTimeToOffsetDateTimeConverter());

    return new R2dbcCustomConversions(storeConversions, customConverters);
  }
}
