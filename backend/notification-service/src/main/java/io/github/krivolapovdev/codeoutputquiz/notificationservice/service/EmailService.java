package io.github.krivolapovdev.codeoutputquiz.notificationservice.service;

import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

public interface EmailService {
  Mono<Void> sendEmail(
      @NonNull String recipientEmail, @NonNull String subject, @NonNull String htmlContent);
}
