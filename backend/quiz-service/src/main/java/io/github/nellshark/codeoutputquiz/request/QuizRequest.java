package io.github.nellshark.codeoutputquiz.request;

import io.github.nellshark.codeoutputquiz.enums.DifficultyLevel;
import io.github.nellshark.codeoutputquiz.enums.ProgrammingLanguage;

public record QuizRequest(
    ProgrammingLanguage programmingLanguage, DifficultyLevel difficultyLevel) {}
