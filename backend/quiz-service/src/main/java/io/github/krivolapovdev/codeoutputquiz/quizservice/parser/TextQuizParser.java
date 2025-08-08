package io.github.krivolapovdev.codeoutputquiz.quizservice.parser;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TextQuizParser {
  private static final Pattern CODE_PATTERN =
      Pattern.compile(
          "\\*\\*Code\\*\\*:\\s*(?:```\\w+\\s*)?(.*?)\\s*(?:```)?\\s*(?=\\*\\*Options\\*\\*)",
          Pattern.DOTALL);
  private static final Pattern OPTIONS_PATTERN =
      Pattern.compile("\\*\\*Options\\*\\*:\\s*(.*?)\\s*(?=\\*\\*Answer\\*\\*)", Pattern.DOTALL);
  private static final Pattern ANSWER_PATTERN = Pattern.compile("\\*\\*Answer\\*\\*:\\s*(\\w)");
  private static final Pattern EXPLANATION_PATTERN =
      Pattern.compile("\\*\\*Explanation\\*\\*:\\s*(.*)", Pattern.DOTALL);
  private static final Pattern OPTION_LINE = Pattern.compile("([A-D])\\)\\s*(.*)");

  public QuizView parse(
      @NonNull String text, @NonNull ProgrammingLanguage language, @NonNull DifficultyLevel level) {
    String code = extractGroup(CODE_PATTERN, text);
    String optionsBlock = extractGroup(OPTIONS_PATTERN, text);
    String answerLetter = extractGroup(ANSWER_PATTERN, text);
    String explanation = extractGroup(EXPLANATION_PATTERN, text);

    AnswerChoice correctAnswer = AnswerChoice.valueOf(answerLetter.toUpperCase());

    List<AnswerChoiceData> answerChoices = new ArrayList<>();
    Matcher lineMatcher = OPTION_LINE.matcher(optionsBlock);
    while (lineMatcher.find()) {
      AnswerChoice choice = AnswerChoice.valueOf(lineMatcher.group(1));
      String choiceText = lineMatcher.group(2).trim();
      answerChoices.add(new AnswerChoiceData(choice, choiceText));
    }

    return new QuizView(
        UUID.randomUUID(),
        code.trim(),
        correctAnswer,
        explanation.trim(),
        language,
        level,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        answerChoices);
  }

  private String extractGroup(@NonNull Pattern pattern, @NonNull String text) {
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    throw new IllegalArgumentException("Pattern not found: " + pattern.pattern());
  }
}
