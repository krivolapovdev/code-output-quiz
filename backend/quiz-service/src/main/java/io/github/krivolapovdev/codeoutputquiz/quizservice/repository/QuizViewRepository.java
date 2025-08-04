package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QuizViewRepository extends ReactiveCrudRepository<QuizView, UUID> {
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
  Mono<QuizView> findRandomQuizView(
      @NonNull @Param("programmingLanguage") ProgrammingLanguage programmingLanguage,
      @NonNull @Param("difficultyLevel") DifficultyLevel difficultyLevel);

  @Modifying
  @Query(
      """
          SELECT
              insert_quiz_with_choices(
                  :code,
                  :lang,
                  :level,
                  :correctAnswer::answer_choice,
                  :explanation,
                  :answerChoicesJson::jsonb
              )
          """)
  Mono<Void> insertQuizWithChoices(
      @NonNull @Param("code") String code,
      @NonNull @Param("lang") String lang,
      @NonNull @Param("level") String level,
      @NonNull @Param("correctAnswer") String correctAnswer,
      @NonNull @Param("explanation") String explanation,
      @NonNull @Param("answerChoicesJson") String answerChoicesJson);

  @Query(
      """
          SELECT
              *
          FROM
              quizzes_with_choices AS qwc
          WHERE
              qwc.programming_language = :lang
          AND
              qwc.difficulty_level = :level
          AND NOT EXISTS (
              SELECT
                  1
              FROM
                  solved_quizzes AS sq
              WHERE
                  sq.quiz_id = qwc.id
              AND
                  sq.user_id = :userId
          )
          LIMIT
              1
          """)
  Mono<QuizView> findUserUnsolvedQuiz(
      @NonNull @Param("programmingLanguage") ProgrammingLanguage programmingLanguage,
      @NonNull @Param("difficultyLevel") DifficultyLevel difficultyLevel,
      @NonNull @Param("userId") UUID userId);
}
