package io.github.krivolapovdev.codeoutputquiz.userservice.entity;

import io.github.krivolapovdev.codeoutputquiz.userservice.enums.AnswerChoice;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_solved_quizzes")
@Data
@AllArgsConstructor
public class UserSolvedQuiz {
  @Column("user_id")
  private UUID userId;

  @Column("quiz_id")
  private UUID quizId;

  @Column("selected_answer")
  private AnswerChoice selectedAnswer;

  @Column("solved_at")
  @CreatedDate
  private OffsetDateTime createdAt;
}
