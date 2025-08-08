package io.github.krivolapovdev.codeoutputquiz.quizservice.view;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerChoiceData {
  private AnswerChoice choice;
  private String text;
}
