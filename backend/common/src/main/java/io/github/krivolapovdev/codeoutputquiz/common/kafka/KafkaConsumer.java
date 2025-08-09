package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
  private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
  private final KafkaMessageDispatcher kafkaMessageDispatcher;

  @PostConstruct
  public void consume() {
    reactiveKafkaConsumerTemplate
        .receive()
        .concatMap(
            receiverRecord ->
                kafkaMessageDispatcher
                    .dispatch(receiverRecord.topic(), receiverRecord.value())
                    .then(Mono.fromRunnable(receiverRecord.receiverOffset()::acknowledge))
                    .doOnError(
                        error ->
                            log.warn(
                                "Failed to process message: {}", receiverRecord.value(), error))
                    .onErrorResume(error -> Mono.empty()))
        .doOnError(e -> log.error("Kafka stream error", e))
        .retryWhen(
            Retry.fixedDelay(Long.MAX_VALUE, java.time.Duration.ofMinutes(1))
                .doBeforeRetry(
                    rs -> log.warn("Resubscribing after error (#{})", rs.totalRetries() + 1)))
        .subscribe();
  }
}
