package io.github.krivolapovdev.codeoutputquiz.notificationservice.service;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.config.MailjetEmailProperties;
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
  private final MailjetEmailProperties mailjetEmailProperties;

  @Override
  public Mono<Void> sendEmail(
      @NonNull String recipientEmail, @NonNull String subject, @NonNull String htmlContent) {
    log.info("Sending email to {}: {}", recipientEmail, subject);

    return Mono.fromFuture(
            () ->
                createSendEmailRequest(recipientEmail, subject, htmlContent)
                    .sendAsyncWith(mailjetClient))
        .doOnSuccess(response -> log.info("Email successfully sent to {}", recipientEmail))
        .doOnError(error -> log.error("Failed to send email to {}", recipientEmail, error))
        .then();
  }

  private SendEmailsRequest createSendEmailRequest(
      @NonNull String recipientEmail, @NonNull String subject, @NonNull String htmlContent) {
    TransactionalEmail message =
        TransactionalEmail.builder()
            .from(new SendContact(mailjetEmailProperties.getSenderEmail()))
            .to(new SendContact(recipientEmail))
            .subject(subject)
            .htmlPart(htmlContent)
            .build();

    return SendEmailsRequest.builder().message(message).build();
  }
}
