package io.github.krivolapovdev.codeoutputquiz.userservice.entity;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_quiz_reactions")
@Data
public class UserQuizReaction {
  @Id
  @Column("user_id")
  private UUID userId;

  @Column("quiz_id")
  private UUID quizId;

  @Column("liked")
  private boolean liked;

  @Column("reacted_at")
  private OffsetDateTime reactedAt;

  public UserQuizReaction(UUID userId, UUID quizId, boolean liked) {
    this.userId = userId;
    this.quizId = quizId;
    this.liked = liked;
  }
}
