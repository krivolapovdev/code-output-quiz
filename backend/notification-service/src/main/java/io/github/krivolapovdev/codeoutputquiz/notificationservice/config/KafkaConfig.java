package io.github.krivolapovdev.codeoutputquiz.notificationservice.config;

import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaConsumer;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaMessageDispatcher;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaMessageHandler;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
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
        .subscription(Collections.singletonList(TopicNames.USER_REGISTRATION));
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

  @Bean
  public KafkaMessageDispatcher kafkaMessageDispatcher(
      Map<String, KafkaMessageHandler> topicHandlers) {
    return new KafkaMessageDispatcher(topicHandlers);
  }

  @Bean
  public KafkaConsumer kafkaConsumer(
      ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate,
      KafkaMessageDispatcher kafkaMessageDispatcher) {
    return new KafkaConsumer(reactiveKafkaConsumerTemplate, kafkaMessageDispatcher);
  }
}
