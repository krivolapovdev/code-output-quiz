package io.github.krivolapovdev.codeoutputquiz.notificationservice.config;

import io.github.krivolapovdev.codeoutputquiz.notificationservice.enums.NotificationType;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.handler.EmailNotificationHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationHandlerConfig {
  @Bean
  public Map<NotificationType, EmailNotificationHandler> emailHandlers(
      List<EmailNotificationHandler> list) {
    return list.stream().collect(Collectors.toMap(EmailNotificationHandler::type, h -> h));
  }
}
