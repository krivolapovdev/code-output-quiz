package io.github.nellshark.codeoutputquiz.notificationservice.handler;

import io.github.nellshark.codeoutputquiz.notificationservice.enums.NotificationType;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

public interface EmailNotificationHandler {
  NotificationType type();

  Mono<Void> handleEvent(@NonNull String recipientEmail);
}
