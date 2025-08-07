package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserSolvedQuiz;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserSolvedQuizRepository extends ReactiveCrudRepository<UserSolvedQuiz, UUID> {
  @Modifying
  @Query(
      """
      INSERT INTO
          user_solved_quizzes (user_id, quiz_id, selected_answer)
      VALUES
          (:#{#userSolvedQuiz.userId},
           :#{#userSolvedQuiz.quizId},
           CAST(:#{#userSolvedQuiz.selectedAnswer} AS answer_choice))
      """)
  Mono<Void> addUserSolvedQuiz(@NonNull @Param("userSolvedQuiz") UserSolvedQuiz userSolvedQuiz);
}
