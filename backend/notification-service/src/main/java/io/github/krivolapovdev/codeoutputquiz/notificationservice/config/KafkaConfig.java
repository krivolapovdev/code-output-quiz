package io.github.krivolapovdev.codeoutputquiz.notificationservice.config;

import java.util.Collections;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
public class KafkaConfig {
  @Bean
  public ReceiverOptions<String, String> receiverOptions(KafkaProperties kafkaProperties) {
    return ReceiverOptions.<String, String>create(kafkaProperties.buildConsumerProperties())
        .subscription(Collections.singletonList("email-events"));
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(
      ReceiverOptions<String, String> receiverOptions) {
    return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
  }
}
