package io.github.krivolapovdev.codeoutputquiz.common.kafka.event;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record QuizSolvedEvent(@NonNull UUID userId, @NonNull UUID quizId) {}
