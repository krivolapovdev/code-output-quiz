package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.userservice.AbstractTestcontainers;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserQuizReaction;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class UserQuizReactionRepositoryTest extends AbstractTestcontainers {
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
