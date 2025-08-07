package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
  private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

  public Mono<SenderResult<Void>> send(@NonNull ProducerRecord<String, String> producerRecord) {
    String topic = producerRecord.topic();
    Object key = producerRecord.key();

    log.info("Sending message to topic='{}' with key='{}'", topic, key);

    return reactiveKafkaProducerTemplate
        .send(producerRecord)
        .doOnSuccess(
            result -> log.info("Message sent successfully to topic='{}', key='{}'", topic, key))
        .doOnError(
            error ->
                log.error("Failed to send message to topic='{}', key='{}'", topic, key, error));
  }
}
