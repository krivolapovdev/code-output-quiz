package io.github.krivolapovdev.codeoutputquiz.notificationservice.service;

import com.mailjet.client.MailjetClient;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.factory.SendEmailsRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
class MailjetEmailService implements EmailService {
  private final MailjetClient mailjetClient;
  private final SendEmailsRequestFactory sendEmailsRequestFactory;

  @Override
  public Mono<Void> sendEmail(
      @NonNull String recipientEmail, @NonNull String subject, @NonNull String htmlContent) {
    log.info("Sending email to {}: {}", recipientEmail, subject);

    return Mono.fromFuture(
            () ->
                sendEmailsRequestFactory
                    .create(recipientEmail, subject, htmlContent)
                    .sendAsyncWith(mailjetClient))
        .doOnSuccess(response -> log.info("Email successfully sent to {}", recipientEmail))
        .doOnError(error -> log.error("Failed to send email to {}", recipientEmail, error))
        .then();
  }
}
