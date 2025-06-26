package io.github.krivolapovdev.codeoutputquiz.quizservice.exception;

import java.time.LocalDateTime;

public record ApiError(String message, String path, int status, LocalDateTime timestamp) {}
