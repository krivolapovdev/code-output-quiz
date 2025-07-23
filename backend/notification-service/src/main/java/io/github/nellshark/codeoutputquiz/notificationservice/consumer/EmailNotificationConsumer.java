package io.github.nellshark.codeoutputquiz.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nellshark.codeoutputquiz.notificationservice.enums.NotificationType;
import io.github.nellshark.codeoutputquiz.notificationservice.event.EmailNotificationEvent;
import io.github.nellshark.codeoutputquiz.notificationservice.handler.EmailNotificationHandler;
import io.github.nellshark.codeoutputquiz.notificationservice.service.EmailService;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationConsumer {
  private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumer;
  private final Map<NotificationType, EmailNotificationHandler> handlers;
  private final EmailService emailService;
  private final ObjectMapper objectMapper;

  @PostConstruct
  public void consume() {
    kafkaConsumer
        .receiveAutoAck()
        .flatMap(
            record ->
                Mono.fromCallable(
                        () -> objectMapper.readValue(record.value(), EmailNotificationEvent.class))
                    .doOnNext(
                        event ->
                            log.info(
                                "Received email notification event: type={}, recipient={}",
                                event.type(),
                                event.recipientEmail()))
                    .doOnError(e -> log.error("Failed to deserialize email event", e)))
        .flatMap(this::handleEvent)
        .subscribe();
  }

  private Mono<Void> handleEvent(@NonNull EmailNotificationEvent event) {
    var handler = handlers.get(event.type());
    if (handler == null) {
      log.warn("No handler found for email type: {}", event.type());
      return Mono.empty();
    }

    log.info("Handling email event: {} for {}", event.type(), event.recipientEmail());
    return handler
        .handleEvent(event.recipientEmail())
        .doOnSuccess(
            unused -> log.info("Email of type {} sent to {}", event.type(), event.recipientEmail()))
        .doOnError(
            e ->
                log.error(
                    "Failed to send {} email to {}", event.type(), event.recipientEmail(), e));
  }
}
