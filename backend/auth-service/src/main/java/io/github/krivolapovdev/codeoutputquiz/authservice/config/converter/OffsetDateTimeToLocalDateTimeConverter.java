package io.github.krivolapovdev.codeoutputquiz.authservice.config.converter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class OffsetDateTimeToLocalDateTimeConverter
    implements Converter<OffsetDateTime, LocalDateTime> {
  @Override
  public LocalDateTime convert(OffsetDateTime source) {
    return source.toLocalDateTime();
  }
}
