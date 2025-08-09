package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.test.publisher.TestPublisher;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {
  @Mock private ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
  @Mock private KafkaMessageDispatcher kafkaMessageDispatcher;
  @InjectMocks private KafkaConsumer kafkaConsumer;
  private TestPublisher<ReceiverRecord<String, String>> testPublisher;

  @BeforeEach
  void setUp() {
    testPublisher = TestPublisher.createCold();
    when(reactiveKafkaConsumerTemplate.receive()).thenReturn(testPublisher.flux());
  }

  @Test
  void shouldAckAfterSuccessfulDispatch() {
    ReceiverOffset offset = mock(ReceiverOffset.class);
    ReceiverRecord<String, String> r1 = mockReceiverRecord("topic-A", "m1", offset);

    when(kafkaMessageDispatcher.dispatch("topic-A", "m1")).thenReturn(Mono.empty());

    kafkaConsumer.consume();
    testPublisher.next(r1);
    testPublisher.complete();

    verify(kafkaMessageDispatcher).dispatch("topic-A", "m1");
    verify(offset).acknowledge();
  }

  @Test
  void shouldSkipFailedMessageWithoutAckAndContinueWithNext() {
    ReceiverOffset badOffset = mock(ReceiverOffset.class);
    ReceiverRecord<String, String> bad = mockReceiverRecord("topic-A", "bad", badOffset);

    ReceiverOffset goodOffset = mock(ReceiverOffset.class);
    ReceiverRecord<String, String> good = mockReceiverRecord("topic-A", "good", goodOffset);

    when(kafkaMessageDispatcher.dispatch("topic-A", "bad"))
        .thenReturn(Mono.error(new RuntimeException("boom")));
    when(kafkaMessageDispatcher.dispatch("topic-A", "good")).thenReturn(Mono.empty());

    kafkaConsumer.consume();

    testPublisher.next(bad);
    testPublisher.next(good);
    testPublisher.complete();

    verify(kafkaMessageDispatcher).dispatch("topic-A", "bad");
    verify(badOffset, never()).acknowledge();

    verify(kafkaMessageDispatcher).dispatch("topic-A", "good");
    verify(goodOffset).acknowledge();
  }

  private static ReceiverRecord<String, String> mockReceiverRecord(
      String topic, String value, ReceiverOffset offset) {

    @SuppressWarnings("unchecked")
    ReceiverRecord<String, String> receiverRecord = mock(ReceiverRecord.class);

    when(receiverRecord.topic()).thenReturn(topic);
    when(receiverRecord.value()).thenReturn(value);
    when(receiverRecord.receiverOffset()).thenReturn(offset);

    return receiverRecord;
  }
}
