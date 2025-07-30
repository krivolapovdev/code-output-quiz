package io.github.krivolapovdev.codeoutputquiz.userservice.event;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record QuizSolvedEvent(@NonNull UUID userId, @NonNull UUID quizId) {}
