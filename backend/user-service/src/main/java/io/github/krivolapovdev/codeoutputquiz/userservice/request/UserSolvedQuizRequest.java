package io.github.krivolapovdev.codeoutputquiz.userservice.request;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserSolvedQuizRequest(@NotNull UUID quizId, @NotNull AnswerChoice selectedAnswer) {}
