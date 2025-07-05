package io.github.krivolapovdev.codeoutputquiz.authservice.config.converter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class LocalDateTimeToOffsetDateTimeConverter
    implements Converter<LocalDateTime, OffsetDateTime> {
  @Override
  public OffsetDateTime convert(LocalDateTime source) {
    return source.atZone(ZoneId.systemDefault()).toOffsetDateTime();
  }
}
