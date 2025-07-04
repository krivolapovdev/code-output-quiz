package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import io.lettuce.core.dynamic.annotation.Param;
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
      WHERE
          programming_language = :programmingLanguage
      AND
          difficulty_level = :difficultyLevel
      ORDER BY
        random()
      LIMIT
        1
      """)
  Mono<QuizView> findRandomQuizView(
      @Param("programmingLanguage") ProgrammingLanguage programmingLanguage,
      @Param("difficultyLevel") DifficultyLevel difficultyLevel);
}
