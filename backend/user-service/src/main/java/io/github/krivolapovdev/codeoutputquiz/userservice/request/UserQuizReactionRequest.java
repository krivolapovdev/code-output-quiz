package io.github.krivolapovdev.codeoutputquiz.userservice.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserQuizReactionRequest(@NotNull UUID quizId, boolean liked) {}
