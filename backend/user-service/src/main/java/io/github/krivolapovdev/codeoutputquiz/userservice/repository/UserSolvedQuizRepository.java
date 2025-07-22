package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserSolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.userservice.enums.AnswerChoice;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserSolvedQuizRepository extends ReactiveCrudRepository<UserSolvedQuiz, UUID> {
  @Query(
      """
          SELECT
              *
          FROM
              user_solved_quizzes
          WHERE
              user_id = :userId
          """)
  Flux<UserSolvedQuiz> findAllSolvedQuizzesByUserId(@Param("userId") UUID userId);

  @Modifying
  @Query(
      """
          INSERT INTO
              user_solved_quizzes (user_id, quiz_id, selected_answer)
          VALUES
              (:userId, :quizId, :selectedAnswer::answer_choice)
          """)
  Mono<Void> addUserSolvedQuiz(
      @Param("userId") UUID userId,
      @Param("quizId") UUID quizId,
      @Param("selectedAnswer") AnswerChoice selectedAnswer);
}
