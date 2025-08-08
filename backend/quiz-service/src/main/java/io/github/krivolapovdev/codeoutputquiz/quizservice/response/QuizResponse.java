package io.github.krivolapovdev.codeoutputquiz.quizservice.response;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.AnswerChoiceData;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

public record QuizResponse(
    @NonNull UUID id,
    @NonNull String code,
    @NonNull AnswerChoice correctAnswer,
    @NonNull String explanation,
    @NonNull ProgrammingLanguage programmingLanguage,
    @NonNull DifficultyLevel difficultyLevel,
    @NonNull List<AnswerChoiceData> answerChoices) {}
