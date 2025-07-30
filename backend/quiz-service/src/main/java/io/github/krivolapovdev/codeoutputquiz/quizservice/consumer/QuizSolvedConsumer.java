package io.github.krivolapovdev.codeoutputquiz.quizservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.event.QuizSolvedEvent;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.SolvedQuizService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class QuizSolvedConsumer {
  private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumer;
  private final ObjectMapper objectMapper;
  private final SolvedQuizService solvedQuizService;

  @PostConstruct
  public void consume() {
    kafkaConsumer
        .receiveAutoAck()
        .doOnNext(record -> log.info("Consumed record: {}", record.value()))
        .flatMap(
            record ->
                Mono.fromCallable(
                        () -> objectMapper.readValue(record.value(), QuizSolvedEvent.class))
                    .doOnNext(
                        event ->
                            log.info(
                                "Parsed QuizSolvedEvent: userId={}, quizId={}",
                                event.userId(),
                                event.quizId()))
                    .flatMap(
                        event ->
                            solvedQuizService.saveUserSolvedQuiz(event.userId(), event.quizId()))
                    .onErrorResume(
                        e -> {
                          log.error(
                              "Failed to deserialize or process QuizSolvedEvent: {}",
                              record.value(),
                              e);
                          return Mono.empty(); // skip this record
                        }))
        .subscribe();
  }
}
