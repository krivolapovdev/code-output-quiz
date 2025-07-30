package io.github.krivolapovdev.codeoutputquiz.notificationservice.service;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.config.MailjetEmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailjetEmailService implements EmailService {
  private final MailjetClient mailjetClient;
  private final MailjetEmailProperties mailjetEmailProperties;

  @Override
  public Mono<Void> sendEmail(String to, String subject, String htmlContent) {
    log.info("Sending email to {}: {}", to, subject);

    TransactionalEmail message =
        TransactionalEmail.builder()
            .from(new SendContact(mailjetEmailProperties.getSenderEmail()))
            .to(new SendContact(to))
            .subject(subject)
            .htmlPart(htmlContent)
            .build();

    SendEmailsRequest request = SendEmailsRequest.builder().message(message).build();

    return Mono.fromCallable(
            () -> {
              SendEmailsResponse response = request.sendWith(mailjetClient);
              log.info("Mailjet email sent to {}: {}", to, response.getMessages());
              return response;
            })
        .doOnError(e -> log.error("Failed to send email to {}", to, e))
        .then();
  }
}
