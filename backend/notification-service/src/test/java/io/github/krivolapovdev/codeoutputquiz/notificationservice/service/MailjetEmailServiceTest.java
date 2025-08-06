package io.github.krivolapovdev.codeoutputquiz.notificationservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.factory.SendEmailsRequestFactory;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class MailjetEmailServiceTest {
  @Mock private MailjetClient mailjetClient;
  @Mock private SendEmailsRequestFactory sendEmailsRequestFactory;
  @InjectMocks private MailjetEmailService mailjetEmailService;

  @Test
  void shouldSendEmailSuccessfully() {
    String to = "test@example.com";
    String subject = "Test Subject";
    String content = "<p>Test Content</p>";

    SendEmailsRequest request = mock(SendEmailsRequest.class);
    SendEmailsResponse response = mock(SendEmailsResponse.class);

    when(sendEmailsRequestFactory.create(to, subject, content)).thenReturn(request);
    when(request.sendAsyncWith(mailjetClient))
        .thenReturn(CompletableFuture.completedFuture(response));

    StepVerifier.create(mailjetEmailService.sendEmail(to, subject, content)).verifyComplete();

    verify(sendEmailsRequestFactory).create(to, subject, content);
    verify(request).sendAsyncWith(mailjetClient);
    verifyNoMoreInteractions(sendEmailsRequestFactory, request, mailjetClient);
  }
}
