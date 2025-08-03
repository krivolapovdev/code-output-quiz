package io.github.krivolapovdev.codeoutputquiz.notificationservice.consumer;

import io.github.krivolapovdev.codeoutputquiz.notificationservice.handler.KafkaMessageHandler;
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
public class KafkaConsumer {
  private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
  private final Map<String, KafkaMessageHandler> topicHandlers;

  @PostConstruct
  public void consume() {
    reactiveKafkaConsumerTemplate
        .receiveAutoAck()
        .flatMap(
            consumerRecord -> {
              String topic = consumerRecord.topic();
              String message = consumerRecord.value();

              log.info("Received Kafka message from topic '{}': {}", topic, message);
              return handleEvent(topic, message);
            })
        .subscribe();
  }

  private Mono<Void> handleEvent(@NonNull String topic, @NonNull String kafkaMessage) {
    KafkaMessageHandler handler = topicHandlers.get(topic);

    if (handler == null) {
      return Mono.error(new IllegalStateException("No handler for topic: " + topic));
    }

    log.info("Delegating kafka message from topic '{}' to handler", topic);

    return handler
        .handleEvent(kafkaMessage)
        .doOnSuccess(v -> log.info("Successfully handled message from topic '{}'", topic))
        .doOnError(e -> log.error("Failed to handle message from topic '{}'", topic, e));
  }
}
