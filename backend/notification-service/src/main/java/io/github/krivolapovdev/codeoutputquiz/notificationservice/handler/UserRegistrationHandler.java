package io.github.krivolapovdev.codeoutputquiz.notificationservice.handler;

import io.github.krivolapovdev.codeoutputquiz.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
class UserRegistrationHandler implements KafkaMessageHandler {
  private final EmailService emailService;

  @Override
  public Mono<Void> handleEvent(@NonNull String recipientEmail) {
    log.info("Handling user registration event for {}", recipientEmail);

    String subject = "Welcome to CodeOutputQuiz!";
    String htmlContent = buildContent(recipientEmail);

    return emailService.sendEmail(recipientEmail, subject, htmlContent);
  }

  @Override
  public String topic() {
    return "user.registration";
  }

  private String buildContent(@NonNull String email) {
    return """
      👋 Hello %s,

      Welcome to **CodeOutputQuiz**! We're thrilled to have you on board.

      🎯 What can you do here?
      - Solve interactive programming output questions
      - Level up your coding skills with real-world examples
      - And much more!

      💡 How to get started?
      Just head over to the quiz section and start solving!

      ❤️ Want to contribute or provide feedback?
      We'd love to hear from you. Visit us on GitHub:
      👉 https://github.com/krivolapovdev/code-output-quiz

      Happy coding! 🚀
      """
        .formatted(email);
  }
}
