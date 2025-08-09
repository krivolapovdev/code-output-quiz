package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserSolvedQuiz;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(TestcontainersConfig.class)
class UserSolvedQuizRepositoryTest {
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
