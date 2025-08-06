package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
  private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
  private final KafkaMessageDispatcher kafkaMessageDispatcher;

  @PostConstruct
  public void consume() {
    reactiveKafkaConsumerTemplate
        .receiveAutoAck()
        .flatMap(
            consumerRecord -> {
              String topic = consumerRecord.topic();
              String message = consumerRecord.value();

              log.info("Received Kafka message from topic '{}': {}", topic, message);
              return kafkaMessageDispatcher.dispatch(topic, message);
            })
        .subscribe();
  }
}
