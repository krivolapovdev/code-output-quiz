package io.github.krivolapovdev.codeoutputquiz.userservice.notifier;

import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaProducer;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import io.github.krivolapovdev.codeoutputquiz.userservice.mapper.QuizSolvedEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizSolvedNotifier {
  private final KafkaProducer kafkaProducer;
  private final QuizSolvedEventMapper quizSolvedEventMapper;

  public Mono<SenderResult<Void>> sendQuizSolvedEvent(@NonNull QuizSolvedEvent event) {
    log.info("Sending QuizSolvedEvent: {}", event);
    return Mono.fromCallable(() -> quizSolvedEventMapper.toJson(event))
        .map(json -> new ProducerRecord<>(TopicNames.QUIZ_SOLVED, event.userId().toString(), json))
        .flatMap(kafkaProducer::send)
        .doOnError(error -> log.error("Failed to send QuizSolvedEvent: {}", event, error));
  }
}
