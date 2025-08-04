package io.github.krivolapovdev.codeoutputquiz.quizservice.consumer;

import io.github.krivolapovdev.codeoutputquiz.quizservice.handler.KafkaMessageHandler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
class KafkaMessageDispatcher {
  private final Map<String, KafkaMessageHandler> topicHandlers;

  public Mono<Void> dispatch(@NonNull String topic, @NonNull String kafkaMessage) {
    KafkaMessageHandler handler = topicHandlers.get(topic);

    if (handler == null) {
      return Mono.error(new IllegalStateException("No handler for topic: " + topic));
    }

    log.info("Delegating kafka message from topic '{}' to handler", topic);

    return handler
        .handleEvent(kafkaMessage)
        .doOnSuccess(v -> log.info("Successfully handled message from topic '{}'", topic))
        .doOnError(e -> log.error("Failed to handle message from topic '{}'", topic, e))
        .onErrorResume(e -> Mono.empty());
  }
}
