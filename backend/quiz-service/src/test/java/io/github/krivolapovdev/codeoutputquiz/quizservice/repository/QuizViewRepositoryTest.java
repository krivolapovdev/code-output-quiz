package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.AbstractTestcontainers;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(TestDatabaseConfig.class)
class QuizViewRepositoryTest extends AbstractTestcontainers {
  @Autowired private QuizViewRepository quizViewRepository;
  @Autowired private DatabaseClient databaseClient;

  @BeforeEach
  void setUp() {
    databaseClient.sql("DELETE FROM solved_quizzes").then().block();
    databaseClient.sql("DELETE FROM quizzes").then().block();
  }

  @Test
  void shouldReturnTrueWhenPostgresIsRunning() {
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldInsertQuizWithChoices() {
    String code = "System.out.println(\"Hello World\");";
    String explanation = "This prints Hello World to the console.";
    String answerChoicesJson =
        """
        [
          {"choice": "A", "text": "System.out.println(\\"Hello World\\");"},
          {"choice": "B", "text": "print(\\"Hello World\\")"},
          {"choice": "C", "text": "echo \\"Hello World\\""},
          {"choice": "D", "text": "Console.WriteLine(\\"Hello World\\");"}
        ]
        """;

    quizViewRepository
        .insertQuizWithChoices(
            code,
            ProgrammingLanguage.JAVA.name(),
            DifficultyLevel.BEGINNER.name(),
            AnswerChoice.A.name(),
            explanation,
            answerChoicesJson)
        .as(StepVerifier::create)
        .verifyComplete();
  }

  @Test
  void shouldInsertAndFindRandomQuiz() {
    ProgrammingLanguage language = ProgrammingLanguage.JAVA;
    DifficultyLevel level = DifficultyLevel.BEGINNER;

    String code = "System.out.println(\"Hello, World!\");";
    String explanation = "This is the standard way to print in Java.";
    String correctAnswer = "A";
    String answerChoicesJson =
        """
        [
          {"choice": "A", "text": "System.out.println(\\"Hello, World!\\");"},
          {"choice": "B", "text": "console.log('Hello, World!');"},
          {"choice": "C", "text": "print('Hello, World!')"},
          {"choice": "D", "text": "echo 'Hello, World!'"}
        ]
        """;

    quizViewRepository
        .insertQuizWithChoices(
            code, language.name(), level.name(), correctAnswer, explanation, answerChoicesJson)
        .then(quizViewRepository.findRandomQuizView(language, level))
        .as(StepVerifier::create)
        .assertNext(
            quiz -> {
              assertThat(quiz.getCode()).isEqualTo(code);
              assertThat(quiz.getProgrammingLanguage()).isEqualTo(language);
              assertThat(quiz.getDifficultyLevel()).isEqualTo(level);
            })
        .verifyComplete();
  }
}
