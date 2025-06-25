package io.github.krivolapovdev.codeoutputquiz.quizservice.entity;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("quizzes")
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

  @Column("programming_language_id")
  private Long programmingLanguageId;

  @Column("difficulty_level_id")
  private Long difficultyLevelId;

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;

  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;
}
