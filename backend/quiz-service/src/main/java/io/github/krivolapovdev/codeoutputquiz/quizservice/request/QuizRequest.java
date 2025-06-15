package io.github.krivolapovdev.codeoutputquiz.quizservice.request;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;

public record QuizRequest(
    ProgrammingLanguage programmingLanguage, DifficultyLevel difficultyLevel) {}
