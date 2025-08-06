package io.github.krivolapovdev.codeoutputquiz.authservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.UserRegistrationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRegistrationEventMapperTest {
  @Mock private ObjectMapper objectMapper;

  @InjectMocks private UserRegistrationEventMapper userRegistrationEventMapper;

  @Test
  void shouldConvertEventToJson() throws Exception {
    var event = new UserRegistrationEvent("test@example.com");
    var expectedJson =
        """
        {"email":"test@example.com"}
        """
            .trim();

    when(objectMapper.writeValueAsString(event)).thenReturn(expectedJson);

    String result = userRegistrationEventMapper.toJson(event);

    assertThat(result).isEqualTo(expectedJson);

    ArgumentCaptor<UserRegistrationEvent> captor =
        ArgumentCaptor.forClass(UserRegistrationEvent.class);
    verify(objectMapper).writeValueAsString(captor.capture());
    assertThat(captor.getValue().recipientEmail()).isEqualTo("test@example.com");
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenSerializationFails() throws Exception {
    var event = new UserRegistrationEvent("fail@example.com");
    when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("boom") {});

    Throwable thrown = catchThrowable(() -> userRegistrationEventMapper.toJson(event));

    assertThat(thrown)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to serialize")
        .hasCauseInstanceOf(JsonProcessingException.class);

    verify(objectMapper).writeValueAsString(event);
  }
}
