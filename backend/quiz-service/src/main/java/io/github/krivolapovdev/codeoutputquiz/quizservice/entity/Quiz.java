package io.github.krivolapovdev.codeoutputquiz.quizservice.entity;

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
  @Id private UUID id;

  @Column("programming_language_id")
  private Long programmingLanguageId;

  @Column("difficulty_level_id")
  private Long difficultyLevelId;

  @Column("answer")
  private String answer;

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;

  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;
}
