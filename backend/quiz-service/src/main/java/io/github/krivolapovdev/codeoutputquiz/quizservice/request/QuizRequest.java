package io.github.krivolapovdev.codeoutputquiz.quizservice.request;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import jakarta.validation.constraints.NotNull;

public record QuizRequest(
    @NotNull ProgrammingLanguage programmingLanguage, @NotNull DifficultyLevel difficultyLevel) {}
