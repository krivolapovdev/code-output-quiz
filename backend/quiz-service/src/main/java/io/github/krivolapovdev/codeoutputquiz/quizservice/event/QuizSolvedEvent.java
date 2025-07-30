package io.github.krivolapovdev.codeoutputquiz.quizservice.event;

import java.util.UUID;

public record QuizSolvedEvent(UUID userId, UUID quizId) {}
