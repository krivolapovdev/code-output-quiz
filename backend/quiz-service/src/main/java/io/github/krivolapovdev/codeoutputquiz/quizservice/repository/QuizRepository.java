package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.Quiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QuizRepository extends ReactiveCrudRepository<Quiz, UUID> {
  @Query(
      """
      SELECT
        *
      FROM
        quizzes_with_choices
      WHERE
          programming_language = :programmingLanguage
      AND
          difficulty_level = :difficultyLevel
      ORDER BY
        random()
      LIMIT
        1
      """)
  Mono<Quiz> findRandomQuizView(
      @Param("programmingLanguage") ProgrammingLanguage programmingLanguage,
      @Param("difficultyLevel") DifficultyLevel difficultyLevel);
}
