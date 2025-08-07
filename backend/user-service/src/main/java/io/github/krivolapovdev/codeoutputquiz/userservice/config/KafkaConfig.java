package io.github.krivolapovdev.codeoutputquiz.userservice.config;

import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaProducer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfig {
  @Bean
  public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(
      KafkaProperties properties) {
    return new ReactiveKafkaProducerTemplate<>(
        SenderOptions.create(properties.buildProducerProperties()));
  }

  @Bean
  public KafkaProducer kafkaProducer(ReactiveKafkaProducerTemplate<String, String> template) {
    return new KafkaProducer(template);
  }
}
