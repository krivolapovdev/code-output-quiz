package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QuizViewRepository extends ReactiveCrudRepository<QuizView, UUID> {
  @Query(
      """
      SELECT
        *
      FROM
        quizzes_view
      ORDER BY
        random()
      LIMIT
        1
      """)
  Mono<QuizView> findRandomQuizView();
}
