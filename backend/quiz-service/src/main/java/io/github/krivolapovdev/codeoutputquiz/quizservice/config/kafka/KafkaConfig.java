package io.github.krivolapovdev.codeoutputquiz.quizservice.config.kafka;

import io.github.krivolapovdev.codeoutputquiz.quizservice.handler.KafkaMessageHandler;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
class KafkaConfig {
  @Bean
  public ReceiverOptions<String, String> receiverOptions(KafkaProperties kafkaProperties) {
    return ReceiverOptions.<String, String>create(kafkaProperties.buildConsumerProperties())
        .subscription(Collections.singletonList(TopicNames.QUIZ_SOLVED));
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(
      ReceiverOptions<String, String> receiverOptions) {
    return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
  }

  @Bean
  public Map<String, KafkaMessageHandler> topicHandlers(List<KafkaMessageHandler> handlers) {
    return handlers.stream()
        .collect(Collectors.toMap(KafkaMessageHandler::topic, handler -> handler));
  }
}
