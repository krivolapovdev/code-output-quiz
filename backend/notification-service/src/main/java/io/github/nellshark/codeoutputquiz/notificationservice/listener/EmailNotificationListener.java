package io.github.nellshark.codeoutputquiz.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nellshark.codeoutputquiz.notificationservice.event.EmailNotificationEvent;
import io.github.nellshark.codeoutputquiz.notificationservice.service.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationListener {
  private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumer;
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
                    .doOnError(e -> log.error("Failed to deserialize email event", e)))
        .doOnNext(event -> log.info("Received email event for recipient: {}", event.to()))
        .flatMap(
            event ->
                emailService
                    .sendEmail(event.to(), event.subject(), event.content())
                    .doOnError(e -> log.error("Failed to send email", e)))
        .subscribe();
  }
}
