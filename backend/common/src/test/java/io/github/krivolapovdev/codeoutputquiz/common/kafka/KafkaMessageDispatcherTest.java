package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class KafkaMessageDispatcherTest {

  @Mock private KafkaMessageHandler topicAHandler;

  @Mock private KafkaMessageHandler topicBHandler;

  @Test
  void shouldDelegateToMatchingHandlerAndComplete() {
    Map<String, KafkaMessageHandler> handlers = Map.of("topic-A", topicAHandler);
    KafkaMessageDispatcher dispatcher = new KafkaMessageDispatcher(handlers);

    when(topicAHandler.handleEvent("hello")).thenReturn(Mono.empty());

    dispatcher.dispatch("topic-A", "hello").as(StepVerifier::create).verifyComplete();

    verify(topicAHandler).handleEvent("hello");
  }

  @Test
  void shouldPropagateHandlerError() {
    Map<String, KafkaMessageHandler> handlers = Map.of("topic-A", topicAHandler);
    KafkaMessageDispatcher dispatcher = new KafkaMessageDispatcher(handlers);

    RuntimeException boom = new RuntimeException("boom");
    when(topicAHandler.handleEvent("bad")).thenReturn(Mono.error(boom));

    dispatcher
        .dispatch("topic-A", "bad")
        .as(StepVerifier::create)
        .expectErrorMatches(e -> e == boom)
        .verify();

    verify(topicAHandler).handleEvent("bad");
  }

  @Test
  void shouldErrorWhenNoHandlerForTopic() {
    Map<String, KafkaMessageHandler> handlers = Map.of("topic-A", topicAHandler);
    KafkaMessageDispatcher dispatcher = new KafkaMessageDispatcher(handlers);

    Mono<Void> result = dispatcher.dispatch("unknown-topic", "msg");

    assertThatThrownBy(result::block)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler for topic: unknown-topic");
  }
}
