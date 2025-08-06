package io.github.krivolapovdev.codeoutputquiz.notificationservice.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.config.MailjetEmailProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailjetSendEmailsRequestFactoryTest {
  @Mock private MailjetEmailProperties mailjetEmailProperties;
  @InjectMocks private MailjetSendEmailsRequestFactory mailjetSendEmailsRequestFactory;

  @Test
  void shouldCreateValidSendEmailsRequest() {
    String senderEmail = "sender@example.com";
    String recipientEmail = "recipient@example.com";
    String subject = "Test Subject";
    String html = "<p>Hello, World!</p>";

    when(mailjetEmailProperties.getSenderEmail()).thenReturn(senderEmail);

    SendEmailsRequest actual =
        mailjetSendEmailsRequestFactory.create(recipientEmail, subject, html);

    TransactionalEmail expectedEmail =
        TransactionalEmail.builder()
            .from(new SendContact(senderEmail))
            .to(new SendContact("recipient@example.com"))
            .subject(subject)
            .htmlPart(html)
            .build();

    SendEmailsRequest expected = SendEmailsRequest.builder().message(expectedEmail).build();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}
