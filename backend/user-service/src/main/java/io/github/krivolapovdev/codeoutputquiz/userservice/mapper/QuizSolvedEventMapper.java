package io.github.krivolapovdev.codeoutputquiz.userservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizSolvedEventMapper {
  private final ObjectMapper objectMapper;

  public @NonNull String toJson(@NonNull QuizSolvedEvent event) {
    try {
      return objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to serialize QuizSolvedEvent to JSON", e);
    }
  }
}
