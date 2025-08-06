package io.github.krivolapovdev.codeoutputquiz.authservice.notifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.mapper.UserRegistrationEventMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaProducer;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.UserRegistrationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    var event = new UserRegistrationEvent(email);

    when(userRegistrationEventMapper.toJson(event)).thenReturn(json);
    when(kafkaProducer.sendEventToTopic(TopicNames.USER_REGISTRATION, email, json))
        .thenReturn(Mono.empty());

    userRegistrationNotifier.notify(email).as(StepVerifier::create).verifyComplete();

    verify(userRegistrationEventMapper).toJson(event);
    verify(kafkaProducer).sendEventToTopic(TopicNames.USER_REGISTRATION, email, json);
  }

  @Test
  void shouldNotFailWhenKafkaProducerThrowsError() {
    String email = "fail@example.com";
    String json = "{\"email\":\"fail@example.com\"}";

    when(userRegistrationEventMapper.toJson(any(UserRegistrationEvent.class))).thenReturn(json);
    when(kafkaProducer.sendEventToTopic(TopicNames.USER_REGISTRATION, email, json))
        .thenReturn(Mono.error(new RuntimeException("Kafka error")));

    userRegistrationNotifier.notify(email).as(StepVerifier::create).verifyComplete();

    verify(userRegistrationEventMapper).toJson(any());
    verify(kafkaProducer).sendEventToTopic(TopicNames.USER_REGISTRATION, email, json);
  }
}
