package io.github.krivolapovdev.codeoutputquiz.notificationservice.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

  @Mock private ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

  @Mock private KafkaMessageDispatcher kafkaMessageDispatcher;

  private KafkaConsumer kafkaConsumer;

  @BeforeEach
  void setUp() {
    kafkaConsumer = new KafkaConsumer(reactiveKafkaConsumerTemplate, kafkaMessageDispatcher);
  }

  @Test
  void shouldConsumeKafkaMessages() {
    String topic = "user.registration";
    String message = "test@example.com";

    ConsumerRecord<String, String> consumerRecord =
        new ConsumerRecord<>(topic, 0, 0L, null, message);
    when(reactiveKafkaConsumerTemplate.receiveAutoAck()).thenReturn(Flux.just(consumerRecord));
    when(kafkaMessageDispatcher.dispatch(topic, message)).thenReturn(Mono.empty());

    kafkaConsumer.consume();

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    verify(kafkaMessageDispatcher).dispatch(topicCaptor.capture(), messageCaptor.capture());

    assertThat(topicCaptor.getValue()).isEqualTo(topic);
    assertThat(messageCaptor.getValue()).isEqualTo(message);
  }
}
