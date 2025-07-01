package io.github.krivolapovdev.codeoutputquiz.userservice.repository;

import io.github.krivolapovdev.codeoutputquiz.userservice.entity.SolvedQuiz;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SolvedQuizRepository extends ReactiveCrudRepository<SolvedQuiz, UUID> {
  @Query(
      """
          SELECT
              *
          FROM
              solved_quizzes
          WHERE
              user_id = :userId
          """)
  Flux<SolvedQuiz> findAllSolvedQuizzesByUserId(@Param("userId") UUID userId);
}
