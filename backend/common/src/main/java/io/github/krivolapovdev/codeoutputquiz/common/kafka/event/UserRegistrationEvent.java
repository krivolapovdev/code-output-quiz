package io.github.krivolapovdev.codeoutputquiz.common.kafka.event;

import org.springframework.lang.NonNull;

public record UserRegistrationEvent(@NonNull String recipientEmail) {
  @Override
  public @NonNull String recipientEmail() {
    return recipientEmail.toLowerCase();
  }
}
