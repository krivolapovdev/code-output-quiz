package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserQuizReaction;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(TestcontainersConfig.class)
class UserQuizReactionRepositoryTest {
  @Autowired private UserQuizReactionRepository userQuizReactionRepository;

  @BeforeEach
  void setUp() {
    userQuizReactionRepository.deleteAll().block();
  }

  @Test
  void shouldSaveUserQuizReaction() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    boolean liked = true;

    UserQuizReaction reaction = new UserQuizReaction(userId, quizId, liked);

    userQuizReactionRepository
        .saveUserQuizReaction(reaction)
        .thenMany(userQuizReactionRepository.findAll())
        .as(StepVerifier::create)
        .assertNext(
            saved -> {
              assertThat(saved.getUserId()).isEqualTo(userId);
              assertThat(saved.getQuizId()).isEqualTo(quizId);
              assertThat(saved.isLiked()).isTrue();
            })
        .verifyComplete();
  }
}
