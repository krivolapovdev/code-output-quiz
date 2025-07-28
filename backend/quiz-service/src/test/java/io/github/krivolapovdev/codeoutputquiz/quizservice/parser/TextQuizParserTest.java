package io.github.krivolapovdev.codeoutputquiz.quizservice.parser;

import static org.junit.jupiter.api.Assertions.*;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextQuizParserTest {
  private TextQuizParser parser;

  @BeforeEach
  void setUp() {
    parser = new TextQuizParser();
  }

  @Test
  void shouldParseQuizWithCodeBlockCorrectly() {
    String quizWithCodeBlock =
        """
        **Code**:
        ```java
        System.out.println("Hello World");
        ```
        **Options**:
        A) Prints Hello
        B) Prints Hello World
        C) Compilation error
        D) Runtime error
        **Answer**: B
        **Explanation**:
        System.out.println prints the string "Hello World".
        """;
    QuizView quiz =
        parser.parse(quizWithCodeBlock, ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);

    assertEquals("System.out.println(\"Hello World\");", quiz.getCode());

    assertEquals(AnswerChoice.B, quiz.getCorrectAnswer());

    assertEquals("System.out.println prints the string \"Hello World\".", quiz.getExplanation());

    assertEquals(4, quiz.getAnswerChoices().size());

    List<AnswerChoiceData> choices = quiz.getAnswerChoices();

    assertEquals(AnswerChoice.A, choices.get(0).getChoice());
    assertEquals("Prints Hello", choices.get(0).getText());

    assertEquals(AnswerChoice.B, choices.get(1).getChoice());
    assertEquals("Prints Hello World", choices.get(1).getText());

    assertEquals(AnswerChoice.C, choices.get(2).getChoice());
    assertEquals("Compilation error", choices.get(2).getText());

    assertEquals(AnswerChoice.D, choices.get(3).getChoice());
    assertEquals("Runtime error", choices.get(3).getText());
  }

  @Test
  void shouldParseQuizWithoutCodeBlockCorrectly() {
    String quizWithoutCodeBlock =
        """
        **Code**:
        System.out.println("Hello World");
        **Options**:
        A) Prints Hello
        B) Prints Hello World
        C) Compilation error
        D) Runtime error
        **Answer**: B
        **Explanation**:
        System.out.println prints the string "Hello World".
        """;
    QuizView quiz =
        parser.parse(quizWithoutCodeBlock, ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);

    assertEquals("System.out.println(\"Hello World\");", quiz.getCode());
    assertEquals(AnswerChoice.B, quiz.getCorrectAnswer());
  }

  @Test
  void shouldThrowIfCodeBlockMissing() {
    String badText =
        """
        **Options**:
        A) Hello
        B) World
        **Answer**: A
        **Explanation**:
        ...
        """;

    assertThrows(
        IllegalArgumentException.class,
        () -> parser.parse(badText, ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER));
  }

  @Test
  void shouldThrowIfAnswerMissing() {
    String badText =
        """
        **Code**:
        System.out.println("X");
        **Options**:
        A) Hello
        B) World
        **Explanation**:
        ...
        """;

    assertThrows(
        IllegalArgumentException.class,
        () -> parser.parse(badText, ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER));
  }

  @Test
  void shouldThrowIfAnswerLetterInvalid() {
    String text =
        """
      **Code**:
      System.out.println("X");
      **Options**:
      A) One
      B) Two
      **Answer**: E
      **Explanation**:
      Invalid answer
      """;

    assertThrows(
        IllegalArgumentException.class,
        () -> parser.parse(text, ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER));
  }

  @Test
  void shouldTrimCodeCorrectlyEvenWithExtraSpacesAndBackticks() {
    String text =
        """
      **Code**:
      ```java

      System.out.println("Trimmed");

      ```
      **Options**:
      A) Trim
      B) Trimmed
      C) Compilation
      D) Error
      **Answer**: B
      **Explanation**:
      Extra space test
      """;

    QuizView quiz = parser.parse(text, ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);
    assertEquals("System.out.println(\"Trimmed\");", quiz.getCode());
  }
}
