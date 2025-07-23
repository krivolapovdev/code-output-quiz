package io.github.nellshark.codeoutputquiz.notificationservice.handler;

import io.github.nellshark.codeoutputquiz.notificationservice.enums.NotificationType;
import io.github.nellshark.codeoutputquiz.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WelcomeUserEmailHandler implements EmailNotificationHandler {
  private final EmailService emailService;

  @Override
  public NotificationType type() {
    return NotificationType.WELCOME_USER;
  }

  @Override
  public Mono<Void> handleEvent(@NonNull String recipientEmail) {
    String subject = "Welcome to CodeOutputQuiz!";
    String content =
        """
        Thanks for registering, %s!

        You can now enjoy solving programming output questions 🚀

        Want to contribute or give feedback?
        Visit our GitHub: https://github.com/krivolapovdev/code-output-quiz
        """
            .formatted(recipientEmail);

    return emailService.sendEmail(recipientEmail, subject, content);
  }
}
