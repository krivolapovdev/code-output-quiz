package io.github.krivolapovdev.codeoutputquiz.quizservice.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "solved_quizzes")
@Data
@AllArgsConstructor
public class SolvedQuiz {
  @Column("user_id")
  private UUID userId;

  @Column("quiz_id")
  private UUID quizId;
}
