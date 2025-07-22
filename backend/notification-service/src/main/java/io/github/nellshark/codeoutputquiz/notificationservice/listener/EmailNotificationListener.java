package io.github.nellshark.codeoutputquiz.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nellshark.codeoutputquiz.notificationservice.event.EmailNotificationEvent;
import io.github.nellshark.codeoutputquiz.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationListener {
  private final EmailService emailService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "email-events", groupId = "notification-service")
  public void listen(String json) {
    Mono.fromCallable(() -> objectMapper.readValue(json, EmailNotificationEvent.class))
        .flatMap(
            event -> {
              log.info("Received email event: {}", event);
              return emailService.sendEmail(event.to(), event.subject(), event.content());
            })
        .doOnError(e -> log.error("Failed to process email event", e))
        .subscribe();
  }
}
