package io.github.krivolapovdev.codeoutputquiz.quizservice.handler;

import io.github.krivolapovdev.codeoutputquiz.common.kafka.KafkaMessageHandler;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.SolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizEventMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.SolvedQuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
class QuizSolvedHandler implements KafkaMessageHandler {
  private final QuizEventMapper quizEventMapper;
  private final SolvedQuizService solvedQuizService;

  @Override
  public @NonNull String topic() {
    return TopicNames.QUIZ_SOLVED;
  }

  @Override
  public Mono<Void> handleEvent(@NonNull String solvedQuizJson) {
    log.info("Handling quiz solved event: {}", solvedQuizJson);

    return Mono.fromCallable(() -> quizEventMapper.toQuizSolvedEvent(solvedQuizJson))
        .map(event -> new SolvedQuiz(event.userId(), event.quizId()))
        .flatMap(solvedQuizService::saveSolvedQuiz)
        .doOnError(e -> log.error("Failed to handle quiz solved event: {}", solvedQuizJson, e));
  }
}
