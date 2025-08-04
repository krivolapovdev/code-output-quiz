package io.github.krivolapovdev.codeoutputquiz.quizservice.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import io.r2dbc.postgresql.codec.Json;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
@RequiredArgsConstructor
public class AnswerChoiceDataReadConverter implements Converter<Json, List<AnswerChoiceData>> {
  private final ObjectMapper objectMapper;

  @Override
  public List<AnswerChoiceData> convert(@NonNull Json source) {
    try {
      return objectMapper.readValue(source.asString(), new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to deserialize answer choices from JSON: ", e);
    }
  }
}
