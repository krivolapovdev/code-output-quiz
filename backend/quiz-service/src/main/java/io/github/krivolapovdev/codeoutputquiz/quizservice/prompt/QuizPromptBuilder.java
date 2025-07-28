package io.github.krivolapovdev.codeoutputquiz.quizservice.prompt;

import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QuizPromptBuilder {
  public String buildQuizPrompt(QuizRequest request) {
    log.info("Building prompt for {}", request);

    String language = request.programmingLanguage().name().toLowerCase();
    String level = request.difficultyLevel().name().toLowerCase();

    return """
        Generate a code multiple-choice question in the "%s" programming language at the %s difficulty level.

        Follow this **strict format** exactly and do not add or omit anything. Each section must start with the specified markdown header:

        **Code**:
        ```%s
        ...
        ```
        **Options**:
        A) ...
        B) ...
        C) ...
        D) ...
        **Answer**:
        ...
        **Explanation**:
        ...
        """
        .formatted(language, level, language)
        .trim();
  }
}
