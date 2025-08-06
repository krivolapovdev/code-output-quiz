package io.github.krivolapovdev.codeoutputquiz.authservice.notifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.mapper.UserRegistrationEventMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaProducer;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.UserRegistrationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserRegistrationNotifierTest {
  @Mock private KafkaProducer kafkaProducer;
  @Mock private UserRegistrationEventMapper userRegistrationEventMapper;
  @InjectMocks private UserRegistrationNotifier userRegistrationNotifier;

  @Test
  void shouldSendUserRegistrationNotificationSuccessfully() {
    String email = "test@example.com";
    String json = "{\"email\":\"test@example.com\"}";

    when(userRegistrationEventMapper.toJson(any(UserRegistrationEvent.class))).thenReturn(json);
    when(kafkaProducer.sendEventToTopic(TopicNames.USER_REGISTRATION, email, json))
        .thenReturn(Mono.empty());

    StepVerifier.create(userRegistrationNotifier.notify(email)).verifyComplete();

    ArgumentCaptor<UserRegistrationEvent> eventCaptor =
        ArgumentCaptor.forClass(UserRegistrationEvent.class);
    verify(userRegistrationEventMapper).toJson(eventCaptor.capture());
    assertThat(eventCaptor.getValue().recipientEmail()).isEqualTo(email);

    verify(kafkaProducer).sendEventToTopic(TopicNames.USER_REGISTRATION, email, json);
  }

  @Test
  void shouldNotFailWhenKafkaProducerThrowsError() {
    String email = "fail@example.com";
    String json = "{\"email\":\"fail@example.com\"}";

    when(userRegistrationEventMapper.toJson(any(UserRegistrationEvent.class))).thenReturn(json);
    when(kafkaProducer.sendEventToTopic(TopicNames.USER_REGISTRATION, email, json))
        .thenReturn(Mono.error(new RuntimeException("Kafka error")));

    StepVerifier.create(userRegistrationNotifier.notify(email)).verifyComplete();

    verify(userRegistrationEventMapper).toJson(any());
    verify(kafkaProducer).sendEventToTopic(TopicNames.USER_REGISTRATION, email, json);
  }
}
