package io.github.krivolapovdev.codeoutputquiz.authservice.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.authservice.event.EmailNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationProducer {
  private final ReactiveKafkaProducerTemplate<String, String> kafkaProducer;
  private final ObjectMapper objectMapper;

  public Mono<SenderResult<Void>> sendEmailNotificationEvent(
      @NonNull EmailNotificationEvent event) {
    log.info("Sending email notification event to: {}", event.recipientEmail());
    return Mono.fromCallable(() -> objectMapper.writeValueAsString(event))
        .map(json -> new ProducerRecord<String, String>("email-events", json))
        .flatMap(kafkaProducer::send)
        .doOnError(error -> log.error("Failed to send email event", error));
  }
}
