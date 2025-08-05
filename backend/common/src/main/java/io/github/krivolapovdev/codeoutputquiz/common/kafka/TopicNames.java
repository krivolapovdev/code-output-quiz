package io.github.krivolapovdev.codeoutputquiz.common.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TopicNames {
  public static final String QUIZ_SOLVED = "quiz.solved";
  public static final String USER_REGISTRATION = "user.registration";
}
