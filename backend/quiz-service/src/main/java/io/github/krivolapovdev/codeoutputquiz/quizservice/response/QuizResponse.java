package io.github.krivolapovdev.codeoutputquiz.quizservice.response;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import java.util.UUID;

public record QuizResponse(
    UUID id,
    String code,
    AnswerChoice correctAnswer,
    String explanation,
    ProgrammingLanguage programmingLanguage,
    DifficultyLevel difficultyLevel) {}
