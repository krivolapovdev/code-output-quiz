package io.github.krivolapovdev.codeoutputquiz.quizservice.entity;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("quizzes_with_choices")
@AllArgsConstructor
@Data
public class Quiz {
  @Id
  @Column("id")
  private UUID id;

  @Column("code")
  private String code;

  @Column("correct_answer")
  private AnswerChoice correctAnswer;

  @Column("explanation")
  private String explanation;

  @Column("programming_language")
  private ProgrammingLanguage programmingLanguage;

  @Column("difficulty_level")
  private DifficultyLevel difficultyLevel;

  @Column("created_at")
  private OffsetDateTime createdAt;

  @Column("updated_at")
  private OffsetDateTime updatedAt;

  @Column("answer_choices")
  private List<AnswerChoiceData> answerChoices;
}
