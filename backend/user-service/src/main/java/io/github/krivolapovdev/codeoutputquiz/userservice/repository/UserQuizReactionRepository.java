package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserQuizReaction;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserQuizReactionRepository extends ReactiveCrudRepository<UserQuizReaction, UUID> {
  @Modifying
  @Query(
      """
    INSERT INTO
        user_quiz_reactions (user_id, quiz_id, liked)
    VALUES
        (:#{#reaction.userId}, :#{#reaction.quizId}, :#{#reaction.liked})
    """)
  Mono<Void> saveUserQuizReaction(@NonNull @Param("reaction") UserQuizReaction userQuizReaction);
}
