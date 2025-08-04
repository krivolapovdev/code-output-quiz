package io.github.krivolapovdev.codeoutputquiz.quizservice.prompt;

import static org.junit.jupiter.api.Assertions.*;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuizPromptBuilderTest {
  @InjectMocks private QuizPromptBuilder quizPromptBuilder;

  @Test
  void shouldGeneratePromptWithCorrectFormatAndValues() {
    QuizRequest request = new QuizRequest(ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);

    String prompt = quizPromptBuilder.buildQuizPrompt(request);

    assertNotNull(prompt);
    assertTrue(prompt.contains("Generate a code multiple-choice question"));
    assertTrue(prompt.contains("programming language at the beginner difficulty level"));
    assertTrue(prompt.contains("```java"));
    assertTrue(prompt.contains("**Options**:"));
    assertTrue(prompt.contains("**Answer**:"));
    assertTrue(prompt.contains("**Explanation**:"));
  }

  @Test
  void shouldUseCorrectLanguageAndLevelInPrompt() {
    QuizRequest request = new QuizRequest(ProgrammingLanguage.PYTHON, DifficultyLevel.ADVANCED);

    String prompt = quizPromptBuilder.buildQuizPrompt(request);

    assertTrue(prompt.contains("\"python\" programming language"), "Language not in prompt");

    assertTrue(prompt.contains("at the advanced difficulty level"), "Difficulty not in prompt");
    assertTrue(prompt.contains("```python"), "Markdown language tag incorrect");
  }

  @Test
  void shouldContainAllRequiredSections() {
    QuizRequest request = new QuizRequest(ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);
    String prompt = quizPromptBuilder.buildQuizPrompt(request);

    assertTrue(prompt.contains("**Code**:"), "Missing **Code** section");
    assertTrue(prompt.contains("```java"), "Missing ```java block");
    assertTrue(prompt.contains("**Options**:"), "Missing **Options** section");
    assertTrue(prompt.contains("A) ..."), "Missing A) ...");
    assertTrue(prompt.contains("B) ..."), "Missing B) ...");
    assertTrue(prompt.contains("C) ..."), "Missing C) ...");
    assertTrue(prompt.contains("D) ..."), "Missing D) ...");
    assertTrue(prompt.contains("**Answer**:"), "Missing **Answer** section");
    assertTrue(prompt.contains("**Explanation**:"), "Missing **Explanation** section");
  }

  @Test
  void shouldPromptEndWithoutTrailingWhitespace() {
    QuizRequest request = new QuizRequest(ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);
    String prompt = quizPromptBuilder.buildQuizPrompt(request);

    assertEquals(prompt.trim(), prompt, "Prompt has leading/trailing whitespace");
  }

  @Test
  void shouldWorkForAllLanguagesAndLevels() {
    for (ProgrammingLanguage lang : ProgrammingLanguage.values()) {
      for (DifficultyLevel level : DifficultyLevel.values()) {
        QuizRequest request = new QuizRequest(lang, level);
        String prompt = quizPromptBuilder.buildQuizPrompt(request);

        assertTrue(
            prompt.contains("\"%s\" programming language".formatted(lang.name().toLowerCase())),
            "Missing language: " + lang);
        assertTrue(
            prompt.contains("at the %s difficulty level".formatted(level.name().toLowerCase())),
            "Missing level: " + level);
        assertTrue(
            prompt.contains("```%s".formatted(lang.name().toLowerCase())),
            "Markdown language tag not matching: " + lang);
      }
    }
  }

  @Test
  void shouldPromptHaveCorrectSectionOrder() {
    QuizRequest request = new QuizRequest(ProgrammingLanguage.JAVA, DifficultyLevel.INTERMEDIATE);
    String prompt = quizPromptBuilder.buildQuizPrompt(request);

    int codeIndex = prompt.indexOf("**Code**:");
    int optionsIndex = prompt.indexOf("**Options**:");
    int answerIndex = prompt.indexOf("**Answer**:");
    int explanationIndex = prompt.indexOf("**Explanation**:");

    assertTrue(codeIndex < optionsIndex, "Code section should come before Options");
    assertTrue(optionsIndex < answerIndex, "Options section should come before Answer");
    assertTrue(answerIndex < explanationIndex, "Answer section should come before Explanation");
  }
}
