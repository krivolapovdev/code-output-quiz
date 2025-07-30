package io.github.krivolapovdev.codeoutputquiz.userservice.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.userservice.event.QuizSolvedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizSolvedProducer {
  private final ReactiveKafkaProducerTemplate<String, String> kafkaProducer;
  private final ObjectMapper objectMapper;

  public Mono<SenderResult<Void>> sendQuizSolvedEvent(@NonNull QuizSolvedEvent event) {
    log.info("Sending QuizSolvedEvent: {}", event);
    return Mono.fromCallable(() -> objectMapper.writeValueAsString(event))
        .map(json -> new ProducerRecord<>("quiz.solved", event.userId().toString(), json))
        .flatMap(kafkaProducer::send)
        .doOnError(error -> log.error("Failed to send QuizSolvedEvent: {}", event, error))
        .retry(3);
  }
}
