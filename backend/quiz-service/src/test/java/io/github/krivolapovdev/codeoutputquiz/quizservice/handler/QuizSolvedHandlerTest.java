package io.github.krivolapovdev.codeoutputquiz.quizservice.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.SolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.QuizEventMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.SolvedQuizService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class QuizSolvedHandlerTest {
  @Mock private QuizEventMapper quizEventMapper;
  @Mock private SolvedQuizService solvedQuizService;
  @InjectMocks private QuizSolvedHandler quizSolvedHandler;

  @Test
  void shouldExposeCorrectTopic() {
    assertThat(quizSolvedHandler.topic()).isEqualTo(TopicNames.QUIZ_SOLVED);
  }

  @Test
  void shouldHandleQuizSolvedEventSuccessfully() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    String solvedQuizJson = "{\"userId\":\"" + userId + "\",\"quizId\":\"" + quizId + "\"}";

    QuizSolvedEvent event = new QuizSolvedEvent(userId, quizId);
    SolvedQuiz solvedQuiz = new SolvedQuiz(userId, quizId);

    when(quizEventMapper.toQuizSolvedEvent(solvedQuizJson)).thenReturn(event);
    when(solvedQuizService.saveSolvedQuiz(solvedQuiz)).thenReturn(Mono.empty());

    quizSolvedHandler.handleEvent(solvedQuizJson).as(StepVerifier::create).verifyComplete();

    verify(quizEventMapper).toQuizSolvedEvent(solvedQuizJson);
    verify(solvedQuizService).saveSolvedQuiz(solvedQuiz);
  }

  @Test
  void shouldLogErrorWhenHandlingFails() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    String solvedQuizJson = "{\"userId\":\"" + userId + "\",\"quizId\":\"" + quizId + "\"}";

    QuizSolvedEvent event = new QuizSolvedEvent(userId, quizId);
    SolvedQuiz solvedQuiz = new SolvedQuiz(userId, quizId);

    when(quizEventMapper.toQuizSolvedEvent(solvedQuizJson)).thenReturn(event);
    when(solvedQuizService.saveSolvedQuiz(solvedQuiz))
        .thenReturn(Mono.error(new RuntimeException("DB error")));

    quizSolvedHandler
        .handleEvent(solvedQuizJson)
        .as(StepVerifier::create)
        .expectError(RuntimeException.class)
        .verify();

    verify(quizEventMapper).toQuizSolvedEvent(solvedQuizJson);
    verify(solvedQuizService).saveSolvedQuiz(solvedQuiz);
  }
}
