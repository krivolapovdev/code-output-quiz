package io.github.krivolapovdev.codeoutputquiz.notificationservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.factory.SendEmailsRequestFactory;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class MailjetEmailServiceTest {

  @Mock private MailjetClient mailjetClient;

  @Mock private SendEmailsRequestFactory sendEmailsRequestFactory;

  private MailjetEmailService emailService;

  @BeforeEach
  void setUp() {
    emailService = new MailjetEmailService(mailjetClient, sendEmailsRequestFactory);
  }

  @Test
  void shouldSendEmailSuccessfully() {
    String to = "test@example.com";
    String subject = "Test Subject";
    String content = "<p>Test Content</p>";

    SendEmailsRequest request = mock(SendEmailsRequest.class);
    SendEmailsResponse response = mock(SendEmailsResponse.class);

    ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

    when(sendEmailsRequestFactory.create(
            toCaptor.capture(), subjectCaptor.capture(), contentCaptor.capture()))
        .thenReturn(request);
    when(request.sendAsyncWith(mailjetClient))
        .thenReturn(CompletableFuture.completedFuture(response));

    StepVerifier.create(emailService.sendEmail(to, subject, content)).verifyComplete();

    assertThat(toCaptor.getValue()).isEqualTo(to);
    assertThat(subjectCaptor.getValue()).isEqualTo(subject);
    assertThat(contentCaptor.getValue()).isEqualTo(content);
  }
}
