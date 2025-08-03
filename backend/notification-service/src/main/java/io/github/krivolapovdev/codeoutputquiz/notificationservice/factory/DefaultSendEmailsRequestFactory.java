package io.github.krivolapovdev.codeoutputquiz.notificationservice.factory;

import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.config.MailjetEmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class DefaultSendEmailsRequestFactory implements SendEmailsRequestFactory {
  private final MailjetEmailProperties mailjetEmailProperties;

  @Override
  public SendEmailsRequest create(
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
