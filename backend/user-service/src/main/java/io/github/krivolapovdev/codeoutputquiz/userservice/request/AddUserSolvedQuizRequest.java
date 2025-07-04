package io.github.krivolapovdev.codeoutputquiz.userservice.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddUserSolvedQuizRequest(@NotNull UUID quizId) {}
