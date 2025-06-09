package io.github.nellshark.quizservice.request;

import io.github.nellshark.quizservice.enums.DifficultyLevel;
import io.github.nellshark.quizservice.enums.ProgrammingLanguage;

public record QuizRequest(
    ProgrammingLanguage programmingLanguage, DifficultyLevel difficultyLevel) {}
