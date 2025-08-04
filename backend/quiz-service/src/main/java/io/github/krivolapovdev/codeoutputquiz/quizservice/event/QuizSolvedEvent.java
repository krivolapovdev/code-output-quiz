package io.github.krivolapovdev.codeoutputquiz.quizservice.event;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record QuizSolvedEvent(@NonNull UUID userId, @NonNull UUID quizId) {}
