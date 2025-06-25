package io.github.krivolapovdev.codeoutputquiz.quizservice.response;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.AnswerChoice;
import java.util.UUID;

public record QuizResponse(UUID id, String code, AnswerChoice correctAnswer, String explanation) {}
