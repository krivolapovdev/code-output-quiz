package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.userservice.AbstractTestcontainers;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserSolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.userservice.enums.AnswerChoice;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class UserSolvedQuizRepositoryTest extends AbstractTestcontainers {
  @Autowired private UserSolvedQuizRepository userSolvedQuizRepository;

  @BeforeEach
  void setUp() {
    userSolvedQuizRepository.deleteAll().block();
  }

  @Test
  void shouldAddUserSolvedQuiz() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    AnswerChoice selectedAnswer = AnswerChoice.B;

    UserSolvedQuiz quiz = new UserSolvedQuiz(userId, quizId, selectedAnswer);

    userSolvedQuizRepository
        .addUserSolvedQuiz(quiz)
        .thenMany(userSolvedQuizRepository.findAll())
        .as(StepVerifier::create)
        .assertNext(
            saved -> {
              assertThat(saved.getUserId()).isEqualTo(userId);
              assertThat(saved.getQuizId()).isEqualTo(quizId);
              assertThat(saved.getSelectedAnswer()).isEqualTo(selectedAnswer);
            })
        .verifyComplete();
  }
}
