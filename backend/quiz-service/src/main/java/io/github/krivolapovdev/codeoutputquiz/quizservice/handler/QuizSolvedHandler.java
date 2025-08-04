package io.github.krivolapovdev.codeoutputquiz.quizservice.handler;

import io.github.krivolapovdev.codeoutputquiz.quizservice.config.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.quizservice.event.QuizSolvedEvent;
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
  public String topic() {
    return TopicNames.QUIZ_SOLVED;
  }

  @Override
  public Mono<Void> handleEvent(@NonNull String quizJson) {
    log.info("Handling quiz solved event: {}", quizJson);

    QuizSolvedEvent event = quizEventMapper.toQuizSolvedEvent(quizJson);
    return solvedQuizService
        .saveUserSolvedQuiz(event.userId(), event.quizId())
        .doOnError(e -> log.error("Failed to handle quiz solved event: {}", quizJson, e));
  }
}
