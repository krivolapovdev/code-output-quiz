package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnswerChoicesJsonMapperTest {
  @Mock private ObjectMapper objectMapper;
  @InjectMocks private AnswerChoicesJsonMapper jsonMapper;

  @Test
  void shouldSerializeAnswerChoicesToJson() throws JsonProcessingException {
    List<AnswerChoiceData> choices =
        List.of(
            new AnswerChoiceData(AnswerChoice.A, "int x = 1;"),
            new AnswerChoiceData(AnswerChoice.B, "String s = \"hi\";"));

    String expectedJson =
        """
        [{"choice":"A","text":"int x = 1;"},{"choice":"B","text":"String s = \\"hi\\";"}]
        """;

    when(objectMapper.writeValueAsString(choices)).thenReturn(expectedJson);

    String json = jsonMapper.toJson(choices);

    assertThat(json).isEqualTo(expectedJson);
  }

  @Test
  void shouldThrowIllegalStateExceptionOnJsonProcessingError() throws JsonProcessingException {
    List<AnswerChoiceData> choices =
        List.of(new AnswerChoiceData(AnswerChoice.C, "float y = 5.0f;"));

    when(objectMapper.writeValueAsString(choices))
        .thenThrow(new JsonProcessingException("Serialization failed") {});

    assertThatThrownBy(() -> jsonMapper.toJson(choices))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to serialize answer choices to JSON");
  }
}
