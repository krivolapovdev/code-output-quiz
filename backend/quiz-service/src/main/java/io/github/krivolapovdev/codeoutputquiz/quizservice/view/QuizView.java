package io.github.krivolapovdev.codeoutputquiz.quizservice.view;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("quizzes_view")
@AllArgsConstructor
@Data
public class QuizView {
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

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;

  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;
}
