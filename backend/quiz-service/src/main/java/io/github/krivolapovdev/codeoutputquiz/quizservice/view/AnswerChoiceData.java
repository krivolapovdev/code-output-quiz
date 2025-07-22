package io.github.krivolapovdev.codeoutputquiz.quizservice.view;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerChoiceData {
  private AnswerChoice choice;
  private String text;
}
