package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerChoicesJsonMapper {
  private final ObjectMapper objectMapper;

  public String toJson(List<AnswerChoiceData> choices) {
    try {
      return objectMapper.writeValueAsString(choices);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to serialize answer choices to JSON", e);
    }
  }
}
