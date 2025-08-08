package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizEventMapper {
  private final ObjectMapper objectMapper;

  public @NonNull QuizSolvedEvent toQuizSolvedEvent(@NonNull String json) {
    try {
      return objectMapper.readValue(json, QuizSolvedEvent.class);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to deserialize QuizSolvedEvent", e);
    }
  }
}
