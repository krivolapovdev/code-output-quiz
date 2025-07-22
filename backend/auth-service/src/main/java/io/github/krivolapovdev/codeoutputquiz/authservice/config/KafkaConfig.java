package io.github.krivolapovdev.codeoutputquiz.authservice.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfig {
  @Bean
  public ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate(
      KafkaProperties properties) {
    return new ReactiveKafkaProducerTemplate<>(
        SenderOptions.create(properties.buildProducerProperties()));
  }
}
