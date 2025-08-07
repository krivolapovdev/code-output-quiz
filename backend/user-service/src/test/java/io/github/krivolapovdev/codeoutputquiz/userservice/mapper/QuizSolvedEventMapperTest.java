package io.github.krivolapovdev.codeoutputquiz.userservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuizSolvedEventMapperTest {
  @Mock private ObjectMapper objectMapper;
  @InjectMocks private QuizSolvedEventMapper quizSolvedEventMapper;

  @Test
  void shouldSerializeQuizSolvedEventToJson() throws JsonProcessingException {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    QuizSolvedEvent event = new QuizSolvedEvent(userId, quizId);
    String expectedJson = "{\"userId\":\"" + userId + "\",\"quizId\":\"" + quizId + "\"}";

    when(objectMapper.writeValueAsString(event)).thenReturn(expectedJson);

    String json = quizSolvedEventMapper.toJson(event);

    assertThat(json)
        .isNotNull()
        .contains("\"userId\":\"" + userId + "\"")
        .contains("\"quizId\":\"" + quizId + "\"");
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenSerializationFails() throws JsonProcessingException {
    QuizSolvedEvent event = new QuizSolvedEvent(UUID.randomUUID(), UUID.randomUUID());

    when(objectMapper.writeValueAsString(event))
        .thenThrow(new JsonProcessingException("Serialization error") {});

    assertThatThrownBy(() -> quizSolvedEventMapper.toJson(event))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to serialize QuizSolvedEvent to JSON");
  }
}
