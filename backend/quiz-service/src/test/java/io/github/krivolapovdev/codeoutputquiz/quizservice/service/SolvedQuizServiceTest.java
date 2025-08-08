package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.SolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.SolvedQuizRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SolvedQuizServiceTest {
  @Mock private SolvedQuizRepository solvedQuizRepository;
  @InjectMocks private SolvedQuizService solvedQuizService;

  @Test
  void shouldSaveSolvedQuizAndComplete() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    SolvedQuiz solvedQuiz = new SolvedQuiz(userId, quizId);

    when(solvedQuizRepository.save(solvedQuiz)).thenReturn(Mono.just(solvedQuiz));

    solvedQuizService.saveSolvedQuiz(solvedQuiz).as(StepVerifier::create).verifyComplete();

    verify(solvedQuizRepository, only()).save(solvedQuiz);
  }

  @Test
  void shouldPropagateErrorWhenRepositoryFails() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    SolvedQuiz solvedQuiz = new SolvedQuiz(userId, quizId);

    RuntimeException boom = new RuntimeException("db fail");
    when(solvedQuizRepository.save(solvedQuiz)).thenReturn(Mono.error(boom));

    solvedQuizService
        .saveSolvedQuiz(solvedQuiz)
        .as(StepVerifier::create)
        .expectErrorSatisfies(ex -> assertThat(ex).isSameAs(boom))
        .verify();

    verify(solvedQuizRepository, only()).save(solvedQuiz);
  }
}
