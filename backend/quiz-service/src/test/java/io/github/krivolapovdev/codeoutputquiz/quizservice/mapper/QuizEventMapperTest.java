package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

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
class QuizEventMapperTest {
  @Mock private ObjectMapper objectMapper;
  @InjectMocks private QuizEventMapper quizEventMapper;

  @Test
  void shouldDeserializeJsonToQuizSolvedEvent() throws JsonProcessingException {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    String json =
        """
        {"userId":"%s","quizId":"%s"}
        """
            .formatted(userId, quizId);

    QuizSolvedEvent expectedEvent = new QuizSolvedEvent(userId, quizId);

    when(objectMapper.readValue(json, QuizSolvedEvent.class)).thenReturn(expectedEvent);

    QuizSolvedEvent result = quizEventMapper.toQuizSolvedEvent(json);

    assertThat(result).isEqualTo(expectedEvent);
  }

  @Test
  void shouldThrowIllegalStateExceptionOnDeserializationError() throws JsonProcessingException {
    String invalidJson = "{ invalid json }";

    when(objectMapper.readValue(invalidJson, QuizSolvedEvent.class))
        .thenThrow(new JsonProcessingException("Deserialization error") {});

    assertThatThrownBy(() -> quizEventMapper.toQuizSolvedEvent(invalidJson))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to deserialize QuizSolvedEvent");
  }
}
