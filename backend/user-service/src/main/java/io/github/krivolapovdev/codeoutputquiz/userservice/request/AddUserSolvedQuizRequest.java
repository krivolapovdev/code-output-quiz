package io.github.krivolapovdev.codeoutputquiz.userservice.request;

import io.github.krivolapovdev.codeoutputquiz.userservice.enums.AnswerChoice;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddUserSolvedQuizRequest(@NotNull UUID quizId, AnswerChoice answerChoice) {}
