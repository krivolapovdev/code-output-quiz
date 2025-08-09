package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {
  @Mock private ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;
  @InjectMocks private KafkaProducer kafkaProducer;

  @Test
  void shouldSendMessageAndReturnSenderResult() {
    String topic = "topic-A";
    String key = UUID.randomUUID().toString();
    String value = "payload";

    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
    @SuppressWarnings("unchecked")
    SenderResult<Void> senderResult = org.mockito.Mockito.mock(SenderResult.class);

    when(reactiveKafkaProducerTemplate.send(producerRecord)).thenReturn(Mono.just(senderResult));

    kafkaProducer
        .send(producerRecord)
        .as(StepVerifier::create)
        .expectNext(senderResult)
        .verifyComplete();

    verify(reactiveKafkaProducerTemplate).send(producerRecord);
  }

  @Test
  void shouldPropagateErrorFromTemplate() {
    String topic = "topic-A";
    String key = "key-1";
    String value = "payload";

    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
    RuntimeException runtimeException = new RuntimeException("send failed");

    when(reactiveKafkaProducerTemplate.send(producerRecord))
        .thenReturn(Mono.error(runtimeException));

    kafkaProducer
        .send(producerRecord)
        .as(StepVerifier::create)
        .expectErrorSatisfies(throwable -> assertThat(throwable).isSameAs(runtimeException))
        .verify();

    verify(reactiveKafkaProducerTemplate).send(producerRecord);
  }
}
