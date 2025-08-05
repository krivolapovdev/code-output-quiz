package io.github.krivolapovdev.codeoutputquiz.authservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationEventMapper {
  private final ObjectMapper objectMapper;

  public @NonNull String toJson(@NonNull UserRegistrationEvent event) {
    try {
      return objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to serialize UserRegistrationEvent to JSON", e);
    }
  }
}
