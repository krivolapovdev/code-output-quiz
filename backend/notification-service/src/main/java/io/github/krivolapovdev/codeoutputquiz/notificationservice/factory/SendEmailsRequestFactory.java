package io.github.krivolapovdev.codeoutputquiz.notificationservice.factory;

import com.mailjet.client.transactional.SendEmailsRequest;
import org.springframework.lang.NonNull;

public interface SendEmailsRequestFactory {
  SendEmailsRequest create(
      @NonNull String recipientEmail, @NonNull String subject, @NonNull String htmlContent);
}
