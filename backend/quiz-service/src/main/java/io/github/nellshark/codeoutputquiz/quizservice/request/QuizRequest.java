package io.github.nellshark.codeoutputquiz.quizservice.request;

import io.github.nellshark.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.nellshark.codeoutputquiz.quizservice.enums.ProgrammingLanguage;

public record QuizRequest(
    ProgrammingLanguage programmingLanguage, DifficultyLevel difficultyLevel) {}
