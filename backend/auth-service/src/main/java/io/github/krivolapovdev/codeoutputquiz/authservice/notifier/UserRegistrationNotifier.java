package io.github.krivolapovdev.codeoutputquiz.authservice.notifier;

import io.github.krivolapovdev.codeoutputquiz.authservice.mapper.UserRegistrationEventMapper;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaProducer;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationNotifier {
  private final KafkaProducer kafkaProducer;
  private final UserRegistrationEventMapper userRegistrationEventMapper;

  public Mono<SenderResult<Void>> notify(@NonNull String email) {
    log.info("Sending user registration notification for email: {}", email);

    var event = new UserRegistrationEvent(email);

    return Mono.fromCallable(() -> userRegistrationEventMapper.toJson(event))
        .map(json -> new ProducerRecord<>(TopicNames.USER_REGISTRATION, email, json))
        .flatMap(kafkaProducer::send)
        .doOnSuccess(
            ignored ->
                log.info("Successfully sent user registration notification for email: {}", email))
        .doOnError(
            error ->
                log.warn(
                    "Failed to send user registration notification for email: {}", email, error))
        .onErrorResume(e -> Mono.empty());
  }
}
