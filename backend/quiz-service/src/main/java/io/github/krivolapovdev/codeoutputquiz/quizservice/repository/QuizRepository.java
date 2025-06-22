package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.Quiz;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QuizRepository extends ReactiveCrudRepository<Quiz, UUID> {
  @Query("""
      SELECT
        *
      FROM
        quizzes
      ORDER BY
        random()
      LIMIT
        1
      """)
  Mono<Quiz> findRandomQuiz();
}
