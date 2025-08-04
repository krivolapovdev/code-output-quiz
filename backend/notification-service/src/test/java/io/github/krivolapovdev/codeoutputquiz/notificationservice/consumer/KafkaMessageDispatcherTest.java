package io.github.krivolapovdev.codeoutputquiz.notificationservice.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.notificationservice.handler.KafkaMessageHandler;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class KafkaMessageDispatcherTest {
  @Mock private KafkaMessageHandler kafkaMessageHandler;

  private KafkaMessageDispatcher kafkaMessageDispatcher;

  @BeforeEach
  void setUp() {
    kafkaMessageDispatcher = new KafkaMessageDispatcher(Map.of("test.topic", kafkaMessageHandler));
  }

  @Test
  void shouldDelegateToKafkaMessageHandler() {
    String topic = "test.topic";
    String kafkaMessage = "test@example.com";

    when(kafkaMessageHandler.handleEvent(kafkaMessage)).thenReturn(Mono.empty());

    StepVerifier.create(kafkaMessageDispatcher.dispatch(topic, kafkaMessage)).verifyComplete();

    ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
    verify(kafkaMessageHandler).handleEvent(kafkaMessageCaptor.capture());

    assertThat(kafkaMessageCaptor.getValue()).isEqualTo(kafkaMessage);
  }

  @Test
  void shouldFailWhenKafkaMessageHandlerIsMissing() {
    String topic = "unknown.topic";
    String kafkaMessage = "test@example.com";

    StepVerifier.create(kafkaMessageDispatcher.dispatch(topic, kafkaMessage))
        .expectErrorSatisfies(
            exception ->
                assertThat(exception)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("No handler for topic: " + topic))
        .verify();
  }

  @Test
  void shouldPropagateErrorFromKafkaMessageHandler() {
    String topic = "test.topic";
    String kafkaMessage = "fail@example.com";

    when(kafkaMessageHandler.handleEvent(any()))
        .thenReturn(Mono.error(new RuntimeException("Kafka handler error")));

    StepVerifier.create(kafkaMessageDispatcher.dispatch(topic, kafkaMessage)).verifyComplete();

    ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
    verify(kafkaMessageHandler).handleEvent(kafkaMessageCaptor.capture());

    assertThat(kafkaMessageCaptor.getValue()).isEqualTo(kafkaMessage);
  }
}
