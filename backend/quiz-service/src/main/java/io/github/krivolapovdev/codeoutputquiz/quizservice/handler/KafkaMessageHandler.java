package io.github.krivolapovdev.codeoutputquiz.quizservice.handler;

import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

public interface KafkaMessageHandler {
  String topic();

  Mono<Void> handleEvent(@NonNull String kafkaMessage);
}
