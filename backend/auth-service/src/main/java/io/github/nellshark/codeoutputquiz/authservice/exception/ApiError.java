package io.github.nellshark.codeoutputquiz.authservice.exception;

import java.time.LocalDateTime;

public record ApiError(String message, String path, int status, LocalDateTime timestamp) {}
