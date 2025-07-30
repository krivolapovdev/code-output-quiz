package io.github.krivolapovdev.codeoutputquiz.notificationservice.handler;

import io.github.krivolapovdev.codeoutputquiz.notificationservice.enums.NotificationType;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

public interface EmailNotificationHandler {
  NotificationType type();

  Mono<Void> handleEvent(@NonNull String recipientEmail);
}
