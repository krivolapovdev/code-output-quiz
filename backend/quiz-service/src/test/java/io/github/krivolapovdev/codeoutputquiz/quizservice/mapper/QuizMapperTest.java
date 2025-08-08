package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class QuizMapperTest {
  private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);

  @Test
  void shouldMapQuizViewToQuizResponse() {
    UUID quizId = UUID.randomUUID();
    List<AnswerChoiceData> answerChoices =
        List.of(
            new AnswerChoiceData(AnswerChoice.A, "int x = 1;"),
            new AnswerChoiceData(AnswerChoice.B, "String s = \"hi\";"));

    QuizView quizView =
        new QuizView(
            quizId,
            "What is the correct syntax?",
            AnswerChoice.A,
            "Because it's the correct Java syntax.",
            ProgrammingLanguage.JAVA,
            DifficultyLevel.BEGINNER,
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            answerChoices);

    QuizResponse response = quizMapper.toResponse(quizView);

    assertThat(response.id()).isEqualTo(quizView.getId());
    assertThat(response.code()).isEqualTo(quizView.getCode());
    assertThat(response.correctAnswer()).isEqualTo(quizView.getCorrectAnswer());
    assertThat(response.explanation()).isEqualTo(quizView.getExplanation());
    assertThat(response.programmingLanguage()).isEqualTo(quizView.getProgrammingLanguage());
    assertThat(response.difficultyLevel()).isEqualTo(quizView.getDifficultyLevel());
    assertThat(response.answerChoices()).isEqualTo(quizView.getAnswerChoices());
  }
}
